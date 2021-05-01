package com.mertgolcu.basicnote.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int,
    @SerializedName("full_name") val fullName: String,
    val email: String,
    val password: String
) : Parcelable
