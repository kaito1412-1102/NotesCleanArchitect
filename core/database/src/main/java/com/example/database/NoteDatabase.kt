package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.dao.NoteDao
import com.example.database.model.NoteEntity

@Database(entities = [NoteEntity::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "note-database"
    }

    abstract fun getNoteDao(): NoteDao
}