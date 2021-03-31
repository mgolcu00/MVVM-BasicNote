package com.mertgolcu.basicnote.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.mertgolcu.basicnote.event.EventType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {


    private val _baseEventChannel = Channel<BaseViewEvent>()
    val baseEvent = _baseEventChannel.receiveAsFlow()

    fun navigate(directions: NavDirections) = viewModelScope.launch {
        _baseEventChannel.send(BaseViewEvent.NavigateTo(directions))
    }

    fun showMessage(msg: String, type: EventType) = viewModelScope.launch {
        _baseEventChannel.send(BaseViewEvent.ShowErrorOrSuccessMessage(msg, type))
    }

    fun showLoading() = viewModelScope.launch {
        _baseEventChannel.send(BaseViewEvent.ShowLoadingDialog(true))
    }

    fun hideLoading() = viewModelScope.launch {
        _baseEventChannel.send(BaseViewEvent.ShowLoadingDialog(false))
    }
    fun popBackStack()=viewModelScope.launch {
        _baseEventChannel.send(BaseViewEvent.NavigateBack())
    }
}