package com.mertgolcu.basicnote.ui.changepassword

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.core.BaseViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.ext.handleHttpException
import com.mertgolcu.basicnote.utils.FILL_REQUIRED_FIELDS
import com.mertgolcu.basicnote.utils.Result
import kotlinx.coroutines.launch

class ChangePasswordViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository
) : BaseViewModel() {

    val password = MutableLiveData("")
    val newPassword = MutableLiveData("")
    val retypeNewPassword = MutableLiveData("")



    fun onSaveClicked() = viewModelScope.launch {
        if (password.value.isNullOrBlank()
            || newPassword.value.isNullOrBlank()
            || retypeNewPassword.value.isNullOrBlank()
        ) {
            showMessage(FILL_REQUIRED_FIELDS, EventType.ERROR)
            return@launch
        }
        showLoading()
        when (val response = repository.updateUserPassword(
            password = password.value!!,
            newPassword = newPassword.value!!,
            newPasswordConfirmation = retypeNewPassword.value!!
        )) {
            is Result.Success -> {
                showMessage(response.response.message, EventType.SUCCESS)
                hideLoading()
            }
            is Result.Error -> {
                showMessage(response.exception.handleHttpException(), EventType.ERROR)
                hideLoading()
            }
        }
    }


}