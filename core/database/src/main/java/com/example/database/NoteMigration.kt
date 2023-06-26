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

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE note_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT NOT NULL, content TEXT NOT NULL, creation_date INTEGER NOT NULL, update_time INTEGER NOT NULL, deadline_time INTEGER NOT NULL DEFAULT 0, status TEXT NOT NULL DEFAULT '')")
            database.execSQL("INSERT INTO note_new (id, title, content, creation_date, update_time, deadline_time, status) SELECT id, title, content, creation_date, update_time, deadline_time, status FROM note")
            database.execSQL("DROP TABLE note")
            database.execSQL("ALTER TABLE note_new RENAME TO note")
        }
    }
}