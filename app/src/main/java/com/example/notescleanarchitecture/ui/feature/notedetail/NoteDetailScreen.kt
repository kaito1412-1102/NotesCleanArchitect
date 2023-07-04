package com.example.notescleanarchitecture.ui.feature.notedetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.model.Note
import com.example.model.Status
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.ui.feature.NoteViewModel
import com.example.notescleanarchitecture.ui.feature.notedetail.component.CalendarDialog
import com.example.notescleanarchitecture.ui.feature.notedetail.component.CustomAlertDialog
import com.example.notescleanarchitecture.ui.feature.notedetail.component.NoteDetail
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState

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
    val openDeleteAlertDialog = remember { mutableStateOf(false) }
    CustomAlertDialog(
        openDialog = openDeleteAlertDialog,
        title = stringResource(id = R.string.title_delete_note),
        msg = stringResource(id = R.string.msg_delete_note),
        positiveButtonText = stringResource(id = R.string.title_yes),
        negativeButtonText = stringResource(id = R.string.title_no),
        positiveButtonClick = {
            note?.let {
                viewModel.removeNote(note)
                navController.popBackStack()
            }
        }
    )

    val openInsertAlertDialog = remember { mutableStateOf(false) }
    CustomAlertDialog(
        openDialog = openInsertAlertDialog,
        title = stringResource(id = R.string.title_add_note_error),
        msg = stringResource(id = R.string.msg_add_note_error),
    )
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if(title.isEmpty() || content.isEmpty()){
                        openInsertAlertDialog.value = true
                        return@FloatingActionButton
                    }else{
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
                    }
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
                    },
                    onDeleteNote = {
                        openDeleteAlertDialog.value = true
                    }
                )
            }
        }
    )
}

