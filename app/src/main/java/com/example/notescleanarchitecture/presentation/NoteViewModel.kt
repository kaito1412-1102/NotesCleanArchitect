package com.example.notescleanarchitecture.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.NoteUseCase
import com.example.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteUseCase: NoteUseCase) : ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun addNote(note: Note) {
        coroutineScope.launch {
            noteUseCase.addNote.invoke(note)
        }
    }

    fun removeNote(note: Note) {
        coroutineScope.launch {
            noteUseCase.removeNote.invoke(note)
        }
    }

    fun searchNotes(input: String): Flow<PagingData<Note>> {
        return noteUseCase.searchNote.invoke(input).cachedIn(viewModelScope)
    }

    fun getAllNotes(): Flow<PagingData<Note>> {
        return noteUseCase.getAllNote.invoke().cachedIn(viewModelScope)
    }
}