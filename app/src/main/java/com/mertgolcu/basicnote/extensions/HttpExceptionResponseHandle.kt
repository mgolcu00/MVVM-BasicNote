package com.mertgolcu.basicnote.extensions

import com.google.gson.Gson
import com.mertgolcu.basicnote.api.ErrorResponse

fun String.handleErrorJson(): ErrorResponse? {
    return Gson().fromJson(
        this,
        ErrorResponse::class.java
    )
}