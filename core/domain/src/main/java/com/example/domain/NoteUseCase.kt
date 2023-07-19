package com.example.domain

import javax.inject.Inject

data class NoteUseCase @Inject constructor(
    val addNote: AddNoteUseCase,
    val getAllNote: GetAllNoteUseCase,
    val getNote: GetNoteUseCase,
    val removeNote: RemoveNoteUseCase,
    val searchNote: SearchNoteUseCase,
    val getFilterNote : GetFilterNoteUseCase,
    val saveFilterNote: SaveFilterNoteUseCase
)