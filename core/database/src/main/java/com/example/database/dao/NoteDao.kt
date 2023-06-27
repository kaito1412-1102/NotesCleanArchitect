package com.example.database.dao

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

    @Query("SELECT * FROM note ORDER BY (CASE WHEN date(deadline_time / 1000, 'unixepoch') = date('now') THEN 0 ELSE 1 END), date(deadline_time / 1000, 'unixepoch') DESC")
    fun getAllNoteEntity(): PagingSource<Int, NoteEntity>

    @Query("SELECT * FROM note WHERE title LIKE '%' || :input || '%' OR status LIKE '%' || :input || '%' ")
    fun searchNoteTitle(input: String): PagingSource<Int, NoteEntity>

    @Delete
    suspend fun deleteNoteEntity(noteEntity: NoteEntity)
}