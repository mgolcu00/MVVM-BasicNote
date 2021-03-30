package com.mertgolcu.basicnote.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//@Parcelize
data class BaseResponse<T>(
    val code: String,
    val data: T,
    val message: String
)
//    : Parcelable
