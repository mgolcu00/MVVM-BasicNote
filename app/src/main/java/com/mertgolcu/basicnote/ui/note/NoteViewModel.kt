package com.mertgolcu.basicnote.ui.note

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.core.BaseViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.ext.handleHttpException
import com.mertgolcu.basicnote.utils.ADD_NOTE
import com.mertgolcu.basicnote.utils.FILL_REQUIRED_FIELDS
import com.mertgolcu.basicnote.utils.Result
import kotlinx.coroutines.launch

class NoteViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    @Assisted val state: SavedStateHandle
) : BaseViewModel() {

    val note = state.get<Note>("note")
    val mode = state.get<String>("mode")
    val title = MutableLiveData(note?.title ?: "")
    val noteText = MutableLiveData(note?.note ?: "")


    fun onClickSave() = viewModelScope.launch {
        if (title.value.isNullOrBlank() || noteText.value.isNullOrBlank()) {
            showMessage(FILL_REQUIRED_FIELDS, EventType.ERROR)
            return@launch
        }
        // repository request
        showLoading()
        when (mode) {
            ADD_NOTE -> {
                val newNote = Note(
                    id = 0,
                    title = title.value!!,
                    note = noteText.value!!
                )
                when (val response =
                    repository.updateNote(
                        newNote.id,
                        newNote.title,
                        newNote.note
                    )) {
                    is Result.Success -> {
                        showMessage(response.response.message, EventType.SUCCESS)
                        hideLoading()
                        popBackStack()
                    }

                    is Result.Error -> {
                        showMessage(response.exception.handleHttpException(), EventType.ERROR)
                        hideLoading()
                    }
                }
            }
            else -> {
                val updatedNote = note?.copy(
                    title = title.value!!,
                    note = noteText.value!!
                )
                if (updatedNote != null) {
                    when (val response =
                        repository.updateNote(
                            updatedNote.id,
                            updatedNote.title,
                            updatedNote.note
                        )) {
                        is Result.Success -> {
                            showMessage(response.response.message, EventType.SUCCESS)
                            hideLoading()
                            popBackStack()
                        }

                        is Result.Error -> {
                            showMessage(response.exception.handleHttpException(), EventType.ERROR)
                            hideLoading()
                        }
                    }
                }
            }
        }
    }
}