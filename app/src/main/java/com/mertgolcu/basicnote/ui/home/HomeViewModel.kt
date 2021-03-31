package com.mertgolcu.basicnote.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.mertgolcu.basicnote.core.BaseViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.data.PreferencesManager
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.ext.handleErrorJson
import com.mertgolcu.basicnote.ext.handleHttpException
import com.mertgolcu.basicnote.utils.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.*

class HomeViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    private val preferences: PreferencesManager,
    @Assisted state: SavedStateHandle
) : BaseViewModel() {

    private val homeEventChannel = Channel<HomeViewEvent>()
    val homeEvent = homeEventChannel.receiveAsFlow()
    private val tokenFlow = preferences.tokenPreferencesFlow
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    private val notesFlow =
        combine(currentQuery.asFlow(), tokenFlow) { query, preferences ->
            Pair(query, preferences)
        }.flatMapLatest { (query, token) ->
            repository.getMyNotes(token.token, query).map {
                it.filter { note ->
                    note.note.toLowerCase(Locale.ROOT)
                        .contains(query.toLowerCase(Locale.ROOT))
                            || note.title.toLowerCase(Locale.ROOT)
                        .contains(query.toLowerCase(Locale.ROOT))
                }
            }.cachedIn(viewModelScope)
        }
    val notes = notesFlow.asLiveData()

    fun searchNotes(query: String) = viewModelScope.launch {
        currentQuery.postValue(query)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        navigate(
            HomeFragmentDirections
                .actionHomeFragmentToDeleteNoteDialog(note)
        )
    }

    fun onClickEdit(note: Note) = viewModelScope.launch {
        navigate(
            HomeFragmentDirections
                .actionHomeFragmentToNoteFragment(note, EDIT_NOTE)
        )
    }

    fun onClickAdd() = viewModelScope.launch {
        navigate(
            HomeFragmentDirections
                .actionHomeFragmentToNoteFragment(null, ADD_NOTE)
        )
    }

    fun onClickNote(note: Note) = viewModelScope.launch {
        navigate(
            HomeFragmentDirections
                .actionHomeFragmentToNoteFragment(note, SHOW_NOTE)
        )
    }

    fun goProfileFragment() = viewModelScope.launch {
        when (val response = repository.getMe(tokenFlow.first().token)) {
            is Result.Success ->
                navigate(
                    HomeFragmentDirections
                        .actionHomeFragmentToProfileFragment(response.response.data)
                )

            is Result.Error ->
                showMessage(response.exception.handleHttpException(), EventType.ERROR)

        }

    }

}