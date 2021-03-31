package com.mertgolcu.basicnote.ui.profile

import com.mertgolcu.basicnote.event.EventType

sealed class ProfileViewEvent {
    //code 0 is Error , 1 is Success
    data class ShowMessageOnSuccessOrError(val msg: String, val code: EventType) : ProfileViewEvent()
    object NavigateLoginForSignOut : ProfileViewEvent()
}
