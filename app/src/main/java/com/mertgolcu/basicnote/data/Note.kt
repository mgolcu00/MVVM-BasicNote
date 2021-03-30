package com.mertgolcu.basicnote.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    val id: Int,
    val title: String,
    val note: String
) : Parcelable
