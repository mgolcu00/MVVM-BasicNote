package com.mertgolcu.basicnote.api

import android.os.Parcelable
import com.mertgolcu.basicnote.data.Note
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PageResponse(
    val data: List<Note>
) : Parcelable
