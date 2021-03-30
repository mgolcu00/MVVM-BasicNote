package com.mertgolcu.basicnote.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//@Parcelize
//data class LoginResponse(
//    val code: String,
//    val data: LoginResponseData,
//    val message: String
//) : Parcelable {
//}

@Parcelize
data class LoginResponseData(
    val access_token: String,
    val token_type: String
) : Parcelable

