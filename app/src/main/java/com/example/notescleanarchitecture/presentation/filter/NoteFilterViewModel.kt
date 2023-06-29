package com.example.notescleanarchitecture.presentation.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.NoteRepository
import com.example.model.DeadlineTag
import com.example.model.DeadlineTagFilter
import com.example.model.NotesFilterSettings
import com.example.model.Status
import com.example.model.StatusFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteFilterViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private var noteFilterSettings: NotesFilterSettings? = null
    private var _uiState = MutableSharedFlow<NoteFilterUiState>()
    val uiState = _uiState.asSharedFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            noteRepository.notesFilterSetting.collect {
                noteFilterSettings = it
                _uiState.emit(NoteFilterUiState.FilterState(it.deadlineTag, it.status))
                Log.d("tuanminh", "$it: ")
            }
        }
    }

    fun saveFilter(deadlineTagFilter: DeadlineTagFilter, statusFilter: StatusFilter) {
        viewModelScope.launch(ioDispatcher) {
            noteRepository.saveFilter(deadlineTagFilter, statusFilter)
        }
    }

    sealed interface NoteFilterUiState {
        data class FilterState(val deadlineTag: DeadlineTagFilter, val status: StatusFilter) : NoteFilterUiState
    }
}