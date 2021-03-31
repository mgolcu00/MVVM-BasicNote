package com.mertgolcu.basicnote.ui.signup

sealed class SignUpViewEvent {
    data class ShowSignUpErrorMessage(val msg: String) : SignUpViewEvent()
    data class SignUpSuccess(val msg: String) : SignUpViewEvent()
    object NavigateToLoginScreen : SignUpViewEvent()
}

