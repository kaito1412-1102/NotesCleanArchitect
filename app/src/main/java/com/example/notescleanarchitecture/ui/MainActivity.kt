package com.example.notescleanarchitecture.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.model.Note
import com.example.notescleanarchitecture.presentation.NoteViewModel
import com.example.notescleanarchitecture.presentation.notelist.NotesListScreen
import com.example.notescleanarchitecture.ui.theme.NotesCleanArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesCleanArchitectureTheme {
                val viewModel = hiltViewModel<NoteViewModel>()
                val notes = viewModel.getAllNotes().collectAsLazyPagingItems()
                NotesApp(notes = notes)
            }
        }
    }
}

@Composable
private fun NotesApp(notes: LazyPagingItems<Note>) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "notes_list") {
        composable(route = "notes_list") {
            NotesListScreen(notes) {
                Log.d("tuanminh", "NotesApp click $it")
            }
        }
        composable(route = "note_detail") {

        }
    }
}