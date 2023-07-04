package com.example.notescleanarchitecture.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.notescleanarchitecture.navigation.SetupNavGraph
import com.example.notescleanarchitecture.ui.feature.NoteViewModel
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
                val navController = rememberNavController()

                SetupNavGraph(navController = navController, notes = notes)
            }
        }
    }
}