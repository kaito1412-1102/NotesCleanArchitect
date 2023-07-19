package com.example.domain

import com.example.data.repository.NoteRepository
import javax.inject.Inject

class GetFilterNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    operator fun invoke() = noteRepository.getFilterNote()
}