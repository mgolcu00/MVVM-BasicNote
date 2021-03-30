package com.mertgolcu.basicnote.event

sealed class LoginEvent  {
    data class ShowLoginErrorMessage(val msg: String?) : LoginEvent()
    data class LoginSuccess(val msg: String) : LoginEvent()
    data class NavigateToSignUpScreen(val email: String, val password: String) : LoginEvent()
}

