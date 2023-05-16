package com.example.domain

import com.example.data.repository.NoteRepository
import com.example.model.Note
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note) = noteRepository.add(note)
}