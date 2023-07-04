package com.example.notescleanarchitecture.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.model.Note
import com.example.notescleanarchitecture.ui.feature.NoteViewModel
import com.example.notescleanarchitecture.ui.feature.notedetail.NoteDetailScreen
import com.example.notescleanarchitecture.ui.feature.notelist.NotesListScreen
import com.example.notescleanarchitecture.utils.Constants

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.NotesList.route) {
        composable(route = Screen.NotesList.route) {
            val viewModel = hiltViewModel<NoteViewModel>()
            val notes = viewModel.getAllNotes().collectAsLazyPagingItems()
            NotesListScreen(notes, navController)
        }
        composable(route = Screen.NoteDetail.route) {
            val note = navController.previousBackStackEntry?.savedStateHandle?.get<Note>(Constants.ARG_NOTE)

            Log.d("tuanminh", "NoteDetailScreen 1: $note")
            NoteDetailScreen(navController, note)
        }
    }
}