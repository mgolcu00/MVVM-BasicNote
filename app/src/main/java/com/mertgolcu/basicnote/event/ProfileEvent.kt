package com.mertgolcu.basicnote.event

sealed class ProfileEvent {
    //code 0 is Error , 1 is Success
    data class ShowMessageOnSuccessOrError(val msg: String, val code: EventType) : ProfileEvent()
    object NavigateLoginForSignOut : ProfileEvent()
}
