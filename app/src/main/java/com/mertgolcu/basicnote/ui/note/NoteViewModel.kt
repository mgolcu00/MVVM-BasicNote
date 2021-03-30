package com.mertgolcu.basicnote.ui.note

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.data.PreferencesManager
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.event.NoteEvent
import com.mertgolcu.basicnote.utils.Result
import com.mertgolcu.basicnote.extensions.handleErrorJson
import com.mertgolcu.basicnote.utils.ADD_NOTE
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NoteViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    @Assisted val state: SavedStateHandle,
    private val preferences: PreferencesManager
) : ViewModel() {

    private val tokenFlow = preferences.tokenPreferencesFlow

    private val noteEventChannel = Channel<NoteEvent>()
    val noteEvent = noteEventChannel.receiveAsFlow()

    val note = state.get<Note>("note")
    val mode = state.get<String>("mode")
    val title = MutableLiveData<String>(note?.title ?: "")
    val noteText = MutableLiveData<String>(note?.note ?: "")


    fun onClickSave() = viewModelScope.launch {
        if (title.value.isNullOrBlank() || noteText.value.isNullOrBlank()) {
            noteEventChannel.send(
                NoteEvent.ShowMessageOnSuccessOrError(
                    "empty",
                    EventType.ERROR
                )
            )
            return@launch
        }
        // repository request
        if (mode == ADD_NOTE) {

            val newNote = Note(
                id = 0,
                title = title.value!!,
                note = noteText.value!!
            )
            when (val response = repository.createNote(tokenFlow.first().token, newNote)) {
                is Result.Success -> {
                    noteEventChannel.send(
                        NoteEvent.ShowMessageOnSuccessOrError(
                            response.response.message,
                            EventType.SUCCESS
                        )
                    )
                }
                is Result.Error -> {
                    noteEventChannel.send(
                        NoteEvent.ShowMessageOnSuccessOrError(
                            (response.exception as HttpException).response()
                                ?.errorBody()
                                ?.string()
                                ?.handleErrorJson()
                                ?.message!!,
                            EventType.ERROR
                        )
                    )
                }
            }
        } else {
            val updatedNote = note?.copy(
                title = title.value!!,
                note = noteText.value!!
            )
            when (val response = repository.updateNote(tokenFlow.first().token, updatedNote!!)) {
                is Result.Success -> {
                    noteEventChannel.send(
                        NoteEvent.ShowMessageOnSuccessOrError(
                            response.response.message,
                            EventType.SUCCESS
                        )
                    )
                }
                is Result.Error -> {
                    noteEventChannel.send(
                        NoteEvent.ShowMessageOnSuccessOrError(
                            (response.exception as HttpException).response()
                                ?.errorBody()
                                ?.string()
                                ?.handleErrorJson()
                                ?.message!!,
                            EventType.ERROR
                        )
                    )
                }
            }

        }
    }
}