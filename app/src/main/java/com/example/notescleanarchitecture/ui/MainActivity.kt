package com.example.notescleanarchitecture.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.notescleanarchitecture.navigation.SetupNavGraph
import com.example.notescleanarchitecture.ui.feature.NoteViewModel
import com.example.notescleanarchitecture.ui.theme.NotesCleanArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesCleanArchitectureTheme {
                val navController = rememberNavController()
                val shouldUpdateNotes = remember { mutableStateOf(false) }

                LaunchedEffect(key1 = true, block = {
                    Log.d("tuanminh", "onCreate: 1")
                    viewModel.init()
                    viewModel.uiState.collect {
                        when (it) {
                            NoteViewModel.NoteUiState.FilterSettingsApply -> {
                                Log.d("tuanminh", "FilterSettingsApply: ")
                                shouldUpdateNotes.value = true
                                shouldUpdateNotes.value = false
                            }
                        }
                    }
                })
                if (shouldUpdateNotes.value) {
                    Log.d("tuanminh", "update")
                    val notes = viewModel.getAllNotes().collectAsLazyPagingItems()
                    SetupNavGraph(navController = navController, notes = notes)
                }
                val notes = viewModel.getAllNotes().collectAsLazyPagingItems()
                SetupNavGraph(navController = navController, notes = notes)
            }
        }
    }
}