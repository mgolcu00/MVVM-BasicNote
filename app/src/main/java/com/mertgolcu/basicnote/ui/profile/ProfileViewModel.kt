package com.mertgolcu.basicnote.ui.profile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.PreferencesManager
import com.mertgolcu.basicnote.data.User
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.utils.Result
import com.mertgolcu.basicnote.ext.handleErrorJson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    private val preferences: PreferencesManager,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    private val profileEventChannel = Channel<ProfileViewEvent>()
    val profileEvent = profileEventChannel.receiveAsFlow()

    private val tokenFlow = preferences.tokenPreferencesFlow


    val user = state.get<User>("user") ?: User(0, "", "", "")
    val fullNameText = MutableLiveData<String>(user.fullName)
    val emailText = MutableLiveData<String>(user.email)


    fun onSaveButtonClick() = viewModelScope.launch {

        if (fullNameText.value.isNullOrBlank() || emailText.value.isNullOrBlank()) {
            profileEventChannel.send(
                ProfileViewEvent.ShowMessageOnSuccessOrError(
                    "empty",
                    EventType.ERROR
                )
            )
            return@launch
        }

        val updatedUser = user.copy(
            fullName = fullNameText.value!!,
            email = emailText.value!!,
            password = " "
        )

        when (val response = repository.updateUser(
            tokenFlow.first().token,
            updatedUser
        )) {
            is Result.Success -> {
                profileEventChannel.send(
                    ProfileViewEvent.ShowMessageOnSuccessOrError(
                        response.response.message,
                        EventType.SUCCESS
                    )
                )
            }
            is Result.Error -> {
                profileEventChannel.send(
                    ProfileViewEvent.ShowMessageOnSuccessOrError(
                        (response.exception as HttpException)
                            .response()
                            ?.errorBody()
                            ?.string()
                            ?.handleErrorJson()?.message!!,
                        EventType.ERROR
                    )
                )
            }
        }

    }

    fun onSignOutClicked() = viewModelScope.launch {
        preferences.updateToken(" ")
        profileEventChannel.send(ProfileViewEvent.NavigateLoginForSignOut)
    }

}