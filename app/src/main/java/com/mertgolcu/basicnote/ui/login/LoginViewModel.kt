package com.mertgolcu.basicnote.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.core.BaseViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.ext.handleHttpException
import com.mertgolcu.basicnote.utils.*
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository
) : BaseViewModel() {
    /*   private val loginEventChannel = Channel<LoginViewEvent>()

       val loginEvent = loginEventChannel.receiveAsFlow()*/


    val emailText = MutableLiveData<String>("")
    val passwordText = MutableLiveData<String>("")

    fun onClickLogin() = viewModelScope.launch {


        //null or blank check
        if (emailText.value == null || emailText.value.toString()
                .isBlank() || (passwordText.value == null || passwordText.value.toString()
                .isBlank())
        ) {
            showMessage(FILL_REQUIRED_FIELDS, EventType.ERROR)
            return@launch
        }
        // format and length check
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.value!!).matches()) {
            // loginEventChannel.send(LoginViewEvent.ShowLoginErrorMessage(LoginAndRegisterErrorType.EMAIL_FORMAT.name))
            showMessage(EMAIL_FORMAT_ERROR, EventType.ERROR)
            return@launch
        }
        showLoading()
        when (val response = repository.login(emailText.value!!, passwordText.value!!)) {
            is Result.Success -> {
                hideLoading()
                showMessage(response.response.message, EventType.SUCCESS)
                navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
//                loginEventChannel.send(LoginViewEvent.LoginSuccess(response.response.message))
            }
            is Result.Error -> {
                hideLoading()
                showMessage(
                    response.exception.handleHttpException(),
                    EventType.ERROR
                )
//                loginEventChannel.send(
//                    LoginViewEvent.ShowLoginErrorMessage(
//                        when (response.exception) {
//                            is HttpException -> {
//                                response.exception.response()
//                                    ?.errorBody()
//                                    ?.string()
//                                    ?.handleErrorJson()?.message
//                            }
//                            else -> {
//                                response.exception.message
//                            }
//                        }
//                    )
//                )
            }
        }
    }

    fun onClickSignUpNow() = viewModelScope.launch {
        navigate(
            LoginFragmentDirections.actionLoginFragmentToSignUpFragment(
                emailText.value!!,
                passwordText.value!!
            )
        )
    }

    fun navigateToForgotPassword() = viewModelScope.launch {
        navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment(emailText.value!!))
    }
}