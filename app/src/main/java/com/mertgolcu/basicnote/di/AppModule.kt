package com.mertgolcu.basicnote.di

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mertgolcu.basicnote.api.BasicNoteApi
import com.mertgolcu.basicnote.core.BaseFragment
import com.mertgolcu.basicnote.core.BaseViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.IRepository
import com.mertgolcu.basicnote.utils.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
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


//    @Singleton
//    @Provides
//    fun provideBaseFragment(baseFragment: BaseFragment<ViewDataBinding, BaseViewModel>): Fragment {
//        return baseFragment
//    }
}

@Module
@InstallIn(ApplicationComponent::class) // or whatever graph fits your need the best
interface RepositoryModules {
    @Binds
    fun provideBasicNoteRepository(repository: BasicNoteRepository): IRepository
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope