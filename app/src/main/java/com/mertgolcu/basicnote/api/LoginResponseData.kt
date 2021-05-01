package com.mertgolcu.basicnote.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponseData(
    val access_token: String,
    val token_type: String
) : Parcelable

