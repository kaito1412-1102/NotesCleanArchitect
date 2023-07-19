package com.example.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.database.model.NoteEntity

@Dao
interface NoteDao {
    @Insert(onConflict = REPLACE)
    suspend fun addNoteEntity(noteEntity: NoteEntity)

    @Query("SELECT * FROM note WHERE id =:id")
    suspend fun getNoteEntity(id: Long): NoteEntity?

    @RawQuery(observedEntities = [NoteEntity::class])
    fun getAllNoteEntity(query: SupportSQLiteQuery): PagingSource<Int, NoteEntity>

    @RawQuery(observedEntities = [NoteEntity::class])
    fun getAllNoteForNotification(query: SupportSQLiteQuery): List<NoteEntity>

    @Query("SELECT * FROM note WHERE title LIKE '%' || :input || '%' OR status LIKE '%' || :input || '%' ")
    fun searchNoteTitle(input: String): PagingSource<Int, NoteEntity>

    @Delete
    fun deleteNoteEntity(noteEntity: NoteEntity)
}