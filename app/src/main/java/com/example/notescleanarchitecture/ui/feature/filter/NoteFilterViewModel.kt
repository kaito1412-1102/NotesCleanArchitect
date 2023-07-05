package com.example.notescleanarchitecture.ui.feature.filter

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

    private var _deadlineTagState = mutableStateOf(DeadlineTagFilter.ALL)
    val deadlineTagState: State<DeadlineTagFilter> = _deadlineTagState

    private var _statusState = mutableStateOf(StatusFilter.ALL)
    val statusState: State<StatusFilter> = _statusState

    private val _eventFlow = MutableSharedFlow<NoteFilterUiState>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getFilterSettings() {
        viewModelScope.launch {
            noteRepository.notesFilterSetting.collect {
                Log.d("tuanminh", "NoteFilterViewModel: $it ")
                _deadlineTagState.value = it.deadlineTag
                _statusState.value = it.status
            }
        }
    }

    fun setDeadlineTag(deadlineTag: DeadlineTagFilter) {
        _deadlineTagState.value = deadlineTag
    }

    fun setStatus(status: StatusFilter) {
        _statusState.value = status
    }

    fun saveFilter(deadlineTagFilter: DeadlineTagFilter, statusFilter: StatusFilter) {
        viewModelScope.launch {
            noteRepository.saveFilter(deadlineTagFilter, statusFilter)
            _eventFlow.emit(NoteFilterUiState.SaveFilterDone)
        }
    }

    sealed interface NoteFilterUiState {
        object SaveFilterDone : NoteFilterUiState
    }
}