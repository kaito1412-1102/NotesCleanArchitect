package com.example.notescleanarchitecture.presentation.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.NoteUseCase
import com.example.model.DeadlineTagFilter
import com.example.model.StatusFilter
import com.example.notescleanarchitecture.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteFilterViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _uiState = MutableSharedFlow<NoteFilterUiState>()
    val uiState = _uiState.asSharedFlow()

    fun getFilterSettings() {
        viewModelScope.launch(ioDispatcher) {
            noteUseCase.getFilterNote.invoke().collect {
                _uiState.emit(NoteFilterUiState.FilterState(it.deadlineTag, it.status))
                Log.d("tuanminh", "$it: ")
            }
        }
    }

    fun saveFilter(deadlineTagFilter: DeadlineTagFilter, statusFilter: StatusFilter) {
        viewModelScope.launch(ioDispatcher) {
            noteUseCase.saveFilterNote.invoke(deadlineTagFilter, statusFilter)
            _uiState.emit(NoteFilterUiState.SaveFilterDone)
        }
    }

    sealed interface NoteFilterUiState {
        data class FilterState(val deadlineTag: DeadlineTagFilter, val status: StatusFilter) : NoteFilterUiState
        object SaveFilterDone : NoteFilterUiState
    }
}