package com.mertgolcu.basicnote.ui.profile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.core.BaseViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.PreferencesManager
import com.mertgolcu.basicnote.data.User
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.ext.handleHttpException
import com.mertgolcu.basicnote.utils.FILL_REQUIRED_FIELDS
import com.mertgolcu.basicnote.utils.Result
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    private val preferences: PreferencesManager,
    @Assisted state: SavedStateHandle
) : BaseViewModel() {

    private val user = state.get<User>("user") ?: User(0, "", "", "")
    val fullNameText = MutableLiveData(user.fullName)
    val emailText = MutableLiveData(user.email)


    fun onSaveButtonClick() = viewModelScope.launch {

        if (fullNameText.value.isNullOrBlank() || emailText.value.isNullOrBlank()) {
            showMessage(FILL_REQUIRED_FIELDS, EventType.ERROR)
            return@launch
        }
        val updatedUser = user.copy(
            fullName = fullNameText.value!!,
            email = emailText.value!!,
            password = " "
        )
        when (val response = repository.updateUser(updatedUser.fullName, updatedUser.email)) {
            is Result.Success ->
                showMessage(response.response.message, EventType.SUCCESS)

            is Result.Error ->
                showMessage(response.exception.handleHttpException(), EventType.ERROR)

        }
    }

    fun onSignOutClicked() = viewModelScope.launch {
        preferences.updateToken(" ")
        navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
    }

    fun onChangePasswordClick() = viewModelScope.launch {
        navigate(ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment())
    }

}