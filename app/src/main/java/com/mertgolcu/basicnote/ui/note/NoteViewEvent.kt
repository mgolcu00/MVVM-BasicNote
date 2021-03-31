package com.mertgolcu.basicnote.ui.note

import com.mertgolcu.basicnote.event.EventType

sealed class NoteViewEvent {
    data class ShowMessageOnSuccessOrError(val msg: String, val code: EventType) : NoteViewEvent()
}
