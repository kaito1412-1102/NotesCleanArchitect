package com.example.notescleanarchitecture.framework

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.domain.NoteUseCase
import com.example.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteUseCase: NoteUseCase) : ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _noteUiState: MutableStateFlow<NoteUiState> = MutableStateFlow(NoteUiState.Loading)
    val noteUiState = _noteUiState.asStateFlow()

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

    fun getNote(id: Long) {
        coroutineScope.launch {
            noteUseCase.getNote.invoke(id).collect { note ->
                note?.let {
                    _noteUiState.emit(NoteUiState.GetNoteSuccess(it))
                }
            }
        }
    }

    fun getAllNotes() {
        Log.d("tuanminh", "getAllNotes: ")
        coroutineScope.launch {
            _noteUiState.emit(NoteUiState.Loading)
            noteUseCase.getAllNote.invoke().cachedIn(viewModelScope).collect {
                _noteUiState.emit(
                    NoteUiState.GetNotesSuccess(it)
                )
            }
        }
    }

    sealed interface NoteUiState {
        object Loading : NoteUiState
        data class GetNotesSuccess(val notes: PagingData<Note>) : NoteUiState
        data class GetNoteSuccess(val note: Note) : NoteUiState
    }
}