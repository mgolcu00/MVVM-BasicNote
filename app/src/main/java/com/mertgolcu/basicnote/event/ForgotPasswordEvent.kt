package com.mertgolcu.basicnote.event

sealed class ForgotPasswordEvent {
    data class ShowForgotPasswordErrorMessage(val msg:String): ForgotPasswordEvent()
    data class ShowSendEmailSuccess(val msg:String): ForgotPasswordEvent()
}