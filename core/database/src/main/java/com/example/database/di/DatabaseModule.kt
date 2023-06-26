package com.example.database.di

import android.content.Context
import androidx.room.Room
import com.example.database.NoteDatabase
import com.example.database.NoteMigration
import com.example.database.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(context, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME)
            .addMigrations(NoteMigration.MIGRATION_1_2)
            .addMigrations(NoteMigration.MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideNoteDao(databaseService: NoteDatabase): NoteDao {
        return databaseService.getNoteDao()
    }
}