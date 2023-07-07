package com.example.notescleanarchitecture.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.repository.NoteRepository
import com.example.domain.NoteUseCase
import com.example.model.DeadlineTagFilter
import com.example.model.Note
import com.example.model.NotesFilterSettings
import com.example.model.StatusFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteUseCase: NoteUseCase, private val noteRepository: NoteRepository) : ViewModel() {

    private var noteFilterSettings: NotesFilterSettings? = null
    private var _uiState = MutableSharedFlow<NoteUiState>()
    val uiState = _uiState.asSharedFlow()

    fun init(){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.notesFilterSetting.collect {
                Log.d("tuanminh", "NoteViewModel: $it: ")
                noteFilterSettings = it
                _uiState.emit(NoteUiState.FilterSettingsApply)
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteUseCase.addNote.invoke(note)
        }
    }

    fun removeNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteUseCase.removeNote.invoke(note)
        }
    }

    fun searchNotes(input: String): Flow<PagingData<Note>> {
        return noteUseCase.searchNote.invoke(input).cachedIn(viewModelScope)
    }

    fun getAllNotes(): Flow<PagingData<Note>> {
        val deadlineTagFilter = noteFilterSettings?.deadlineTag ?: DeadlineTagFilter.ALL
        val statusFilter = noteFilterSettings?.status ?: StatusFilter.ALL
//        Log.d("tuanminh", "getAllNotes: $deadlineTagFilter - $statusFilter")
        return noteUseCase.getAllNote.invoke(deadlineTagFilter, statusFilter).cachedIn(viewModelScope)
    }

    sealed interface NoteUiState {
        object FilterSettingsApply : NoteUiState
    }
}