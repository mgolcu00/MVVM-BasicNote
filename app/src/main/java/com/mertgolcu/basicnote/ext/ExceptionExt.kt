package com.mertgolcu.basicnote.ext

import retrofit2.HttpException
import java.lang.Exception

fun Exception.handleHttpException(): String {
    return if (this is HttpException) {
        this.response()?.errorBody()?.string()?.handleErrorJson()?.message!!
    } else
        this.message!!
}