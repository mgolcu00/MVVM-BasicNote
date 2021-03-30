package com.mertgolcu.basicnote.ui.changepassword

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.PreferencesManager
import com.mertgolcu.basicnote.event.BaseEvent
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.utils.Result
import com.mertgolcu.basicnote.extensions.handleErrorJson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ChangePasswordViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    private val preferences: PreferencesManager
) : ViewModel() {

    private val changePasswordEventChannel = Channel<BaseEvent>()
    val changePasswordEvent = changePasswordEventChannel.receiveAsFlow()

    val password = MutableLiveData<String>("")
    val newPassword = MutableLiveData<String>("")
    val retypeNewPassword = MutableLiveData<String>("")

    private val tokenFlow = preferences.tokenPreferencesFlow


    fun onSaveClicked() = viewModelScope.launch {
        if (password.value.isNullOrBlank() || newPassword.value.isNullOrBlank() || retypeNewPassword.value.isNullOrBlank()) {
            // send error
            changePasswordEventChannel.send(
                BaseEvent.ShowErrorOrSuccessMessage(
                    "empty",
                    EventType.ERROR
                )
            )
            return@launch
        }
        when (val response = repository.updateUserPassword(
            tokenFlow.first().token,
            password.value!!,
            newPassword.value!!,
            retypeNewPassword.value!!
        )) {
            is Result.Success -> {
                changePasswordEventChannel.send(
                    BaseEvent.ShowErrorOrSuccessMessage(
                        response.response.message, EventType.SUCCESS
                    )
                )
            }
            is Result.Error -> {
                changePasswordEventChannel.send(
                    BaseEvent.ShowErrorOrSuccessMessage(
                        (response.exception as HttpException).response()
                            ?.errorBody()
                            ?.string()
                            ?.handleErrorJson()?.message!!,
                        EventType.ERROR
                    )
                )
            }
        }
    }


}