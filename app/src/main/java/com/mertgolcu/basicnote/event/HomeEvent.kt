package com.mertgolcu.basicnote.event

import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.data.User

sealed class HomeEvent {
    data class NavigateToProfile(val user: User?, val msg: String?) : HomeEvent()
    data class NavigateNote(val note: Note?, val mode: String) : HomeEvent()
    data class NavigateToDeleteNoteScreen(val note: Note) : HomeEvent()
  //  data class ListSearched(val query: String?) : HomeEvent()
}