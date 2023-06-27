package com.example.data.repository

import androidx.paging.PagingData
import com.example.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun add(note: Note)
    fun get(id: Long): Flow<Note?>
    fun getAll(): Flow<PagingData<Note>>
    fun searchNote(input: String): Flow<PagingData<Note>>
    suspend fun remove(note: Note)
}