package com.example.domain

import com.example.data.repository.NoteRepository
import com.example.model.DeadlineTagFilter
import com.example.model.StatusFilter
import javax.inject.Inject

class SaveFilterNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(deadlineTagFilter: DeadlineTagFilter, statusFilter: StatusFilter) = noteRepository.saveFilter(deadlineTagFilter, statusFilter)
}