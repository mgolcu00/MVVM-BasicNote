package com.mertgolcu.basicnote.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorResponse(
    val code: String,
    val message: String
) : Parcelable
