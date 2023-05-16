package com.example.data.di

import com.example.data.repository.NoteRepository
import com.example.data.repository.OfflineNoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
fun interface DataModule {
    @Binds
    fun bindsNoteRepository(noteRepository: OfflineNoteRepository): NoteRepository
}