package com.example.notescleanarchitecture.navigation

sealed class Screen(val route: String) {
    object NotesList : Screen(route = "notes_list")
    object NoteDetail : Screen(route = "note_detail")
    object NoteFilter : Screen(route = "note_filter")
}