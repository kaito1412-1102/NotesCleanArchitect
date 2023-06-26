package com.example.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object NoteMigration {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE note ADD COLUMN deadline_time INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE note ADD COLUMN deadline_tag TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE note ADD COLUMN status TEXT NOT NULL DEFAULT ''")
        }
    }
}