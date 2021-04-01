package com.mertgolcu.basicnote.api

//@Parcelize
data class BaseResponse<T>(
    val code: String,
    val data: T,
    val message: String
)
//    : Parcelable
