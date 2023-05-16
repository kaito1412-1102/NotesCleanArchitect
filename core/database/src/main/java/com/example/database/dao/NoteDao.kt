package com.example.database.dao

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.database.model.NoteEntity

@Dao
interface NoteDao {
    @Insert(onConflict = REPLACE)
    suspend fun addNoteEntity(noteEntity: NoteEntity)

    @Query("SELECT * FROM note WHERE id =:id")
    suspend fun getNoteEntity(id: Long): NoteEntity?

    @Query("SELECT * FROM note LIMIT :limit OFFSET:offset")
    suspend fun getAllNoteEntity(limit: Int, offset: Int): List<NoteEntity>

    @Delete
    suspend fun deleteNoteEntity(noteEntity: NoteEntity)
}