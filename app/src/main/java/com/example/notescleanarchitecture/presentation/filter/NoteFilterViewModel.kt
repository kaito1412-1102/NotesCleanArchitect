package com.example.notescleanarchitecture.presentation.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.NoteRepository
import com.example.model.DeadlineTagFilter
import com.example.model.StatusFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteFilterViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private var _uiState = MutableSharedFlow<NoteFilterUiState>()
    val uiState = _uiState.asSharedFlow()

    fun getFilterSettings(){
        viewModelScope.launch {
            noteRepository.notesFilterSetting.collect {
                _uiState.emit(NoteFilterUiState.FilterState(it.deadlineTag, it.status))
                Log.d("tuanminh", "$it: ")
            }
        }
    }

    fun saveFilter(deadlineTagFilter: DeadlineTagFilter, statusFilter: StatusFilter) {
        viewModelScope.launch {
            noteRepository.saveFilter(deadlineTagFilter, statusFilter)
            _uiState.emit(NoteFilterUiState.SaveFilterDone)
        }
    }

    sealed interface NoteFilterUiState {
        data class FilterState(val deadlineTag: DeadlineTagFilter, val status: StatusFilter) : NoteFilterUiState
        object SaveFilterDone : NoteFilterUiState
    }
}