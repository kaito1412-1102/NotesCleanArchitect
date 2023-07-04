package com.example.notescleanarchitecture.ui.feature.notedetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.model.Note
import com.example.model.Status
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.ui.feature.NoteViewModel
import com.example.notescleanarchitecture.ui.feature.notedetail.component.CalendarDialog
import com.example.notescleanarchitecture.ui.feature.notedetail.component.NoteDetail
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(navController: NavController, note: Note?, viewModel: NoteViewModel = hiltViewModel()) {

    val id = note?.id ?: 0
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var status by remember {
        mutableStateOf(note?.status ?: Status.TODO)
    }
    var deadline by remember { mutableStateOf(note?.deadline ?: System.currentTimeMillis()) }

    val calendarState = rememberUseCaseState(visible = false, onCloseRequest = { })
    CalendarDialog(calendarState = calendarState) {
        deadline = it
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val currentTime = System.currentTimeMillis()
                    val newNote = Note(
                        id = id,
                        title = title,
                        content = content,
                        creationTime = note?.creationTime ?: currentTime,
                        updateTime = currentTime,
                        deadline = deadline,
                        status = status
                    )
                    viewModel.addNote(newNote)
                    navController.popBackStack()
                },
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_check), contentDescription = "Add note")
            }
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NoteDetail(
                    status = status,
                    title = title,
                    deadline = deadline,
                    calendarState = calendarState,
                    content = content,
                    statusChange = {
                        status = it
                    },
                    titleChange = {
                        title = it
                    },
                    contentChange = {
                        content = it
                    }
                )
            }
        }
    )
}

