package com.mertgolcu.basicnote.ui.forgotpassword

sealed class ForgotPasswordEvent {
    data class ShowSendEmailSuccess(val msg:String): ForgotPasswordEvent()
}