package com.mertgolcu.basicnote.di

import com.mertgolcu.basicnote.api.BasicNoteApi
import com.mertgolcu.basicnote.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideBasicNoteApi(retrofit: Retrofit): BasicNoteApi =
        retrofit.create(BasicNoteApi::class.java)

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() =
        CoroutineScope(SupervisorJob()) // Learn This specially "Coroutine"


}



@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope