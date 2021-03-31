package com.mertgolcu.basicnote.ui.home

import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.data.User

sealed class HomeViewEvent {
    data class NavigateToProfile(val user: User?, val msg: String?) : HomeViewEvent()
    data class NavigateNote(val note: Note?, val mode: String) : HomeViewEvent()
    data class NavigateToDeleteNoteScreen(val note: Note) : HomeViewEvent()
}