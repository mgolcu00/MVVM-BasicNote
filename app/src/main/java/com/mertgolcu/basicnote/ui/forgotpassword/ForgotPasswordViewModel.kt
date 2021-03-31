package com.mertgolcu.basicnote.ui.forgotpassword

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.core.BaseViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.event.LoginAndRegisterErrorType
import com.mertgolcu.basicnote.utils.Result
import com.mertgolcu.basicnote.ext.handleErrorJson
import com.mertgolcu.basicnote.ext.handleHttpException
import com.mertgolcu.basicnote.utils.EMAIL_FORMAT_ERROR
import com.mertgolcu.basicnote.utils.FILL_REQUIRED_FIELDS
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ForgotPasswordViewModel @ViewModelInject constructor(

    private val repository: BasicNoteRepository,
    @Assisted state: SavedStateHandle
) : BaseViewModel() {

    private val forgotPasswordEventChannel = Channel<ForgotPasswordEvent>()
    val forgotPasswordEvent = forgotPasswordEventChannel.receiveAsFlow()

    val emailText = MutableLiveData<String>(state.get<String>("email"))

    fun onResetClick() = viewModelScope.launch {
        showLoading()
        if (emailText.value.isNullOrBlank()) {
            showMessage(FILL_REQUIRED_FIELDS, EventType.ERROR)
            return@launch
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.value!!).matches()) {
            showMessage(EMAIL_FORMAT_ERROR, EventType.ERROR)
        }
        when (val response = repository.forgotPassword(emailText.value!!)) {
            is Result.Success -> {
                hideLoading()
                showMessage(response.response.message, EventType.SUCCESS)
                forgotPasswordEventChannel.send(ForgotPasswordEvent.ShowSendEmailSuccess(response.response.message))
            }
            is Result.Error -> {
                hideLoading()
                showMessage(response.exception.handleHttpException(), EventType.ERROR)
            }
        }

    }

}