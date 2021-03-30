package com.mertgolcu.basicnote.event

import com.mertgolcu.basicnote.event.EventType

sealed class NoteEvent {
    data class ShowMessageOnSuccessOrError(val msg: String, val code: EventType) : NoteEvent()
}
