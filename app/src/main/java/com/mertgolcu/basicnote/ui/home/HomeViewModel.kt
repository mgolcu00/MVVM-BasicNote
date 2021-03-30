package com.mertgolcu.basicnote.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.data.PreferencesManager
import com.mertgolcu.basicnote.event.HomeEvent

import com.mertgolcu.basicnote.utils.Result
import com.mertgolcu.basicnote.extensions.handleErrorJson
import com.mertgolcu.basicnote.utils.ADD_NOTE
import com.mertgolcu.basicnote.utils.EDIT_NOTE
import com.mertgolcu.basicnote.utils.SHOW_NOTE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.*

class HomeViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    private val preferences: PreferencesManager,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    private val homeEventChannel = Channel<HomeEvent>()
    val homeEvent = homeEventChannel.receiveAsFlow()
    private val tokenFlow = preferences.tokenPreferencesFlow
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    private val notesFlow =
        combine(currentQuery.asFlow(), tokenFlow) { query, preferences ->
            Pair(query, preferences)
        }.flatMapLatest { (query, token) ->
            repository.getMyNotes(token.token, query).map {
                it.filter { note ->
                    note.note.toLowerCase().contains(query.toLowerCase())
                }
            }.cachedIn(viewModelScope)
        }


    val notes = notesFlow.asLiveData()

    fun searchNotes(query: String) = viewModelScope.launch {
        currentQuery.postValue(query)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        homeEventChannel.send(HomeEvent.NavigateToDeleteNoteScreen(note))
    }

    fun onClickEdit(note: Note) = viewModelScope.launch {
        homeEventChannel.send(HomeEvent.NavigateNote(note, EDIT_NOTE))
    }

    fun onClickAdd() = viewModelScope.launch {
        homeEventChannel.send(HomeEvent.NavigateNote(null, ADD_NOTE))
    }

    fun onClickNote(note: Note) = viewModelScope.launch {
        homeEventChannel.send(HomeEvent.NavigateNote(note, SHOW_NOTE))
    }

    fun goProfileFragment() = viewModelScope.launch {
        when (val response = repository.getMe(tokenFlow.first().token)) {
            is Result.Success -> {
                homeEventChannel.send(HomeEvent.NavigateToProfile(response.response.data, ""))
            }
            is Result.Error -> {
                homeEventChannel.send(
                    HomeEvent.NavigateToProfile(
                        null, (response.exception as HttpException)
                            .response()
                            ?.errorBody()
                            ?.string()
                            ?.handleErrorJson()?.message
                    )
                )
            }
        }

    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = ""
    }
}