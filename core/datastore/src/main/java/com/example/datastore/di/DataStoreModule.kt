package com.example.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.datastore.NotesPreferencesSerializer
import com.example.notescleanarchitecture.NotesFilterPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesNotesPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<NotesFilterPreferences> {
        return DataStoreFactory.create(
            serializer = NotesPreferencesSerializer(),
            produceFile = {
                context.dataStoreFile("notes_filter_preferences.pb")
            }
        )
    }
}