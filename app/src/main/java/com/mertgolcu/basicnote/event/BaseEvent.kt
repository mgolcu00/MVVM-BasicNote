package com.mertgolcu.basicnote.event

sealed class BaseEvent {
    data class ShowErrorOrSuccessMessage(val msg: String, val type: EventType) : BaseEvent()
}
