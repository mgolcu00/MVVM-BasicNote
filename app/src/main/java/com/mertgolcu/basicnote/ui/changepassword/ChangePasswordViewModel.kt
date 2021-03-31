package com.mertgolcu.basicnote.ui.changepassword

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.core.BaseViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.PreferencesManager
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.ext.handleHttpException
import com.mertgolcu.basicnote.utils.FILL_REQUIRED_FIELDS
import com.mertgolcu.basicnote.utils.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChangePasswordViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    private val preferences: PreferencesManager
) : BaseViewModel() {

    val password = MutableLiveData<String>("")
    val newPassword = MutableLiveData<String>("")
    val retypeNewPassword = MutableLiveData<String>("")

    private val tokenFlow = preferences.tokenPreferencesFlow


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
            tokenFlow.first().token,
            password.value!!,
            newPassword.value!!,
            retypeNewPassword.value!!
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