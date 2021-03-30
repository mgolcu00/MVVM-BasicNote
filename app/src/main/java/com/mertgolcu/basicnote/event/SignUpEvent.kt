package com.mertgolcu.basicnote.event

sealed class SignUpEvent {
    data class ShowSignUpErrorMessage(val msg: String) : SignUpEvent()
    data class SignUpSuccess(val msg: String) : SignUpEvent()
    object NavigateToLoginScreen : SignUpEvent()
}

