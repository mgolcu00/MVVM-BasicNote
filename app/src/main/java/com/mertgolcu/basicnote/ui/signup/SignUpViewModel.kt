package com.mertgolcu.basicnote.ui.signup

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.core.BaseViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.utils.EventType
import com.mertgolcu.basicnote.ext.handleHttpException
import com.mertgolcu.basicnote.utils.EMAIL_FORMAT_ERROR
import com.mertgolcu.basicnote.utils.FILL_REQUIRED_FIELDS
import com.mertgolcu.basicnote.utils.Result
import kotlinx.coroutines.launch

class SignUpViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    @Assisted state: SavedStateHandle
) : BaseViewModel() {

    val emailText = MutableLiveData<String>(state.get("email"))
    val passwordText = MutableLiveData<String>(state.get("password"))
    val fullNameText = MutableLiveData<String>()


    fun onSignUpClick() = viewModelScope.launch {

        if (emailText.value.isNullOrBlank()
            || passwordText.value.isNullOrBlank()
            || fullNameText.value.isNullOrBlank()
        ) {
            showMessage(FILL_REQUIRED_FIELDS, EventType.ERROR)
            return@launch
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.value!!).matches()) {
            showMessage(EMAIL_FORMAT_ERROR, EventType.ERROR)
            return@launch
        }
        showLoading()
        when (val response =
            repository.register(
                fullNameText.value!!,
                emailText.value!!,
                passwordText.value!!
            )) {
            is Result.Success -> {
                hideLoading()
                showMessage(response.response.message, EventType.SUCCESS)
                navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
            }
            is Result.Error -> {
                hideLoading()
                showMessage(response.exception.handleHttpException(), EventType.ERROR)
            }

        }
    }

    fun onLoginNowClick() = viewModelScope.launch {
        popBackStack()
    }

}