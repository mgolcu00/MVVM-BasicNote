package com.mertgolcu.basicnote.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.mertgolcu.basicnote.core.BaseViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.utils.EventType
import com.mertgolcu.basicnote.ext.handleHttpException
import com.mertgolcu.basicnote.utils.*
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    @Assisted state: SavedStateHandle
) : BaseViewModel() {

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    private val notesFlow = currentQuery.asFlow().flatMapLatest { query ->
        repository.getMyNotes().map { data: PagingData<Note> ->
            data.filter { note ->
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

        when (val response = repository.getMe()) {
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