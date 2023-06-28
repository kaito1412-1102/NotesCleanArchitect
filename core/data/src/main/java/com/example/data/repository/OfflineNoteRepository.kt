package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.ultils.Constants.PAGE_SIZE
import com.example.database.dao.NoteDao
import com.example.database.model.NoteEntity
import com.example.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineNoteRepository @Inject constructor(private val noteDao: NoteDao) : NoteRepository {
    override suspend fun add(note: Note) {
        noteDao.addNoteEntity(NoteEntity.fromNote(note))
    }

    override fun get(id: Long) = flow {
        val note = noteDao.getNoteEntity(id)?.toNote()
        emit(note)
    }

    override fun getAll() = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            prefetchDistance = 1,
            initialLoadSize = PAGE_SIZE
        ),
        pagingSourceFactory = { noteDao.getAllNoteEntity() }
    ).flow.map { pagingData ->
        pagingData.map {
            it.toNote()
        }
    }

    override fun searchNote(input: String): Flow<PagingData<Note>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            prefetchDistance = 1,
            initialLoadSize = PAGE_SIZE
        ),
        pagingSourceFactory = { noteDao.searchNoteTitle(input) }
    ).flow.map { pagingData ->
        pagingData.map {
            it.toNote()
        }
    }

    override suspend fun remove(note: Note) {
        return noteDao.deleteNoteEntity(NoteEntity.fromNote(note))
    }
}