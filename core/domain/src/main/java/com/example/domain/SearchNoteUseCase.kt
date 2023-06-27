package com.example.domain

import com.example.data.repository.NoteRepository
import javax.inject.Inject

class SearchNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    operator fun invoke(input: String) = noteRepository.searchNote(input)
}