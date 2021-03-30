package com.mertgolcu.basicnote.ui.forgotpassword

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.event.ForgotPasswordEvent
import com.mertgolcu.basicnote.event.LoginAndRegisterErrorType
import com.mertgolcu.basicnote.utils.Result
import com.mertgolcu.basicnote.extensions.handleErrorJson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ForgotPasswordViewModel @ViewModelInject constructor(

    private val repository: BasicNoteRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    private val forgotPasswordEventChannel = Channel<ForgotPasswordEvent>()
    val forgotPasswordEvent = forgotPasswordEventChannel.receiveAsFlow()

    val emailText = MutableLiveData<String>(state.get<String>("email"))

    fun onResetClick() = viewModelScope.launch {
        if (emailText.value == null || emailText.value!!.isBlank()) {
            forgotPasswordEventChannel.send(
                ForgotPasswordEvent.ShowForgotPasswordErrorMessage
                    (LoginAndRegisterErrorType.EMAIL_BLANK.name)
            )
            return@launch
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.value!!).matches()) {
            forgotPasswordEventChannel.send(
                ForgotPasswordEvent.ShowForgotPasswordErrorMessage
                    (LoginAndRegisterErrorType.EMAIL_FORMAT.name)
            )
        }

        when (val response = repository.forgotPassword(emailText.value!!)) {
            is Result.Success -> {
                forgotPasswordEventChannel.send(
                    ForgotPasswordEvent.ShowSendEmailSuccess(
                        response.response.message
                    )
                )
            }
            is Result.Error -> {
                forgotPasswordEventChannel.send(
                    ForgotPasswordEvent.ShowForgotPasswordErrorMessage(
                        (response.exception as HttpException).response()
                            ?.errorBody()
                            ?.string()
                            ?.handleErrorJson()
                            ?.message!!
                    )
                )
            }
        }


        // api call
    }

}