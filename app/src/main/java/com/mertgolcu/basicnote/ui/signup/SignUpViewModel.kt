package com.mertgolcu.basicnote.ui.signup

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.event.LoginAndRegisterErrorType
import com.mertgolcu.basicnote.utils.Result
import com.mertgolcu.basicnote.ext.handleErrorJson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    private val signUpEventChannel = Channel<SignUpViewEvent>()
    val signUpEvent = signUpEventChannel.receiveAsFlow()

    val emailText = MutableLiveData<String>(state.get<String>("email"))
    val passwordText = MutableLiveData<String>(state.get<String>("password"))
    val fullNameText = MutableLiveData<String>()


    fun onSignUpClick() = viewModelScope.launch {


        //null or blank check
        if (emailText.value == null || emailText.value.toString().isBlank()) {
            signUpEventChannel.send(SignUpViewEvent.ShowSignUpErrorMessage(LoginAndRegisterErrorType.EMAIL_BLANK.name))
            return@launch
        }
        if (passwordText.value == null || passwordText.value.toString().isBlank()) {
            signUpEventChannel.send(SignUpViewEvent.ShowSignUpErrorMessage(LoginAndRegisterErrorType.PASSWORD_BLANK.name))
            return@launch
        }
        if (fullNameText.value == null || fullNameText.value.toString().isBlank()) {
            signUpEventChannel.send(SignUpViewEvent.ShowSignUpErrorMessage(LoginAndRegisterErrorType.FULL_NAME_BLANK.name))
            return@launch
        }
        // format and length check
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.value!!).matches()) {
            signUpEventChannel.send(SignUpViewEvent.ShowSignUpErrorMessage(LoginAndRegisterErrorType.EMAIL_FORMAT.name))
            return@launch
        }

        //sign up fun
        when (val response =
            repository.register(fullNameText.value!!, emailText.value!!, passwordText.value!!)) {
            is Result.Success -> {
                signUpEventChannel.send(SignUpViewEvent.SignUpSuccess(response.response.message))
            }
            is Result.Error -> {
                signUpEventChannel.send(
                    SignUpViewEvent.ShowSignUpErrorMessage(
                        (response.exception as HttpException).response()
                            ?.errorBody()
                            ?.string()
                            ?.handleErrorJson()
                            ?.message!!
                    )
                )
            }
        }

    }

    fun onLoginNowClick() = viewModelScope.launch {
        signUpEventChannel.send(SignUpViewEvent.NavigateToLoginScreen)
    }

}