package com.mertgolcu.basicnote.core

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import com.mertgolcu.basicnote.utils.EventType

sealed class BaseViewEvent {
    data class ShowErrorOrSuccessMessage(val msg: String, val type: EventType) : BaseViewEvent()
    data class NavigateTo(val directions: NavDirections) : BaseViewEvent()
    data class ShowLoadingDialog(val show: Boolean) : BaseViewEvent()
    data class NavigateBack(
        @IdRes val destination: Int? = null,
        val result: Bundle? = null
    ) : BaseViewEvent()
}
