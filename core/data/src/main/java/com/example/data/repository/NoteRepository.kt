package com.example.data.repository

import androidx.paging.PagingData
import com.example.database.model.NoteEntity
import com.example.model.DeadlineTagFilter
import com.example.model.Note
import com.example.model.NotesFilterSettings
import com.example.model.StatusFilter
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun add(note: Note)
    fun get(id: Long): Flow<Note?>
    fun getAll(deadlineTagFilter: DeadlineTagFilter, statusFilter: StatusFilter): Flow<PagingData<Note>>
    fun getAllNoteForNotification(deadlineTagFilter: DeadlineTagFilter, statusFilter: StatusFilter): List<Note>
    fun searchNote(input: String): Flow<PagingData<Note>>
    fun remove(note: Note)
    fun getFilterNote(): Flow<NotesFilterSettings>
    suspend fun saveFilter(deadlineTagFilter: DeadlineTagFilter, statusFilter: StatusFilter)
}