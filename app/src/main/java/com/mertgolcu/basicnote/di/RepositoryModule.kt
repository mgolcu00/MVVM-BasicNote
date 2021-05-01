package com.mertgolcu.basicnote.di

import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.DefaultBasicNoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class) // or whatever graph fits your need the best
interface RepositoryModules {
    @Binds
    fun provideBasicNoteRepository(repository: DefaultBasicNoteRepository): BasicNoteRepository

}