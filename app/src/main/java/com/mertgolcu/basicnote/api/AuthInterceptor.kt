package com.mertgolcu.basicnote.api

import com.mertgolcu.basicnote.data.PreferencesManager
import com.mertgolcu.basicnote.di.ApplicationScope
import com.mertgolcu.basicnote.utils.KEY_AUTHORIZATION
import com.mertgolcu.basicnote.utils.KEY_BEARER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val preferences: PreferencesManager,
    @ApplicationScope private val applicationScope: CoroutineScope
) : Interceptor {

    private var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        setToken()
        if (token.isNullOrBlank()) return chain.proceed(builder.build())
        builder.addHeader(KEY_AUTHORIZATION, "$KEY_BEARER $token")
        return chain.proceed(builder.build())
    }

    private fun setToken() = applicationScope.launch {
        token = preferences.getToken()
    }


}