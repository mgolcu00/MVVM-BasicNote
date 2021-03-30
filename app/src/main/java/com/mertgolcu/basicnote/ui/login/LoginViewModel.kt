package com.mertgolcu.basicnote.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.event.LoginEvent
import com.mertgolcu.basicnote.event.LoginAndRegisterErrorType
import com.mertgolcu.basicnote.utils.Result
import com.mertgolcu.basicnote.extensions.handleErrorJson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository
) : ViewModel() {
    private val loginEventChannel = Channel<LoginEvent>()

    val loginEvent = loginEventChannel.receiveAsFlow()


    val emailText = MutableLiveData<String>("")
    val passwordText = MutableLiveData<String>("")

    fun onClickLogin() = viewModelScope.launch {
        //null or blank check
        if (emailText.value == null || emailText.value.toString().isBlank()) {

            loginEventChannel.send(LoginEvent.ShowLoginErrorMessage(LoginAndRegisterErrorType.EMAIL_BLANK.name))
            return@launch
        }
        if (passwordText.value == null || passwordText.value.toString().isBlank()) {
            loginEventChannel.send(LoginEvent.ShowLoginErrorMessage(LoginAndRegisterErrorType.PASSWORD_BLANK.name))
            return@launch
        }
        // format and length check
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.value!!).matches()) {
            loginEventChannel.send(LoginEvent.ShowLoginErrorMessage(LoginAndRegisterErrorType.EMAIL_FORMAT.name))
            return@launch
        }
        when (val response = repository.login(emailText.value!!, passwordText.value!!)) {
            is Result.Success -> {
                loginEventChannel.send(LoginEvent.LoginSuccess(response.response.message))
            }
            is Result.Error -> {
                loginEventChannel.send(
                    LoginEvent.ShowLoginErrorMessage(
                        when (response.exception) {
                            is HttpException -> {
                                response.exception.response()
                                    ?.errorBody()
                                    ?.string()
                                    ?.handleErrorJson()?.message
                            }
                            else -> {
                                response.exception.message
                            }
                        }
                    )
                )
            }
        }
    }

    fun onClickSignUpNow() = viewModelScope.launch {
        loginEventChannel.send(
            LoginEvent.NavigateToSignUpScreen(
                emailText.value!!,
                passwordText.value!!
            )
        )
    }
}