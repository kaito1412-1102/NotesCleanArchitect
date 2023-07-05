package com.example.notescleanarchitecture.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.LazyPagingItems
import com.example.model.Note
import com.example.notescleanarchitecture.ui.feature.filter.NoteFilterScreen
import com.example.notescleanarchitecture.ui.feature.notedetail.NoteDetailScreen
import com.example.notescleanarchitecture.ui.feature.notelist.NotesListScreen
import com.example.notescleanarchitecture.ui.feature.search.SearchScreen
import com.example.notescleanarchitecture.utils.Constants

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    notes: LazyPagingItems<Note>
) {
    NavHost(navController = navController, startDestination = Screen.NotesList.route) {
        composable(route = Screen.NotesList.route) {
            NotesListScreen(notes, navController)
        }
        composable(route = Screen.NoteDetail.route) {
            val note = navController.previousBackStackEntry?.savedStateHandle?.get<Note>(Constants.ARG_NOTE)
            NoteDetailScreen(navController, note)
        }
        composable(route = Screen.NoteFilter.route) {
            NoteFilterScreen(navController)
        }
        composable(route = Screen.SearchNote.route) {
            SearchScreen(navController)
        }
    }
}