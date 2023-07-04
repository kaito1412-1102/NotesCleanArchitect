package com.example.notescleanarchitecture.ui.feature.notelist

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.example.model.DeadlineTag
import com.example.model.Note
import com.example.model.Status
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.extension.formatDateStyle1
import com.example.notescleanarchitecture.navigation.Screen
import com.example.notescleanarchitecture.utils.Constants
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun NotesListScreen(notes: LazyPagingItems<Note>, navController: NavController) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.NoteDetail.route)
            },
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_create), contentDescription = "Add note")
        }
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = true)
            SwipeRefresh(state = swipeRefreshState, onRefresh = {
                notes.refresh()
            }) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Toolbar(notes.itemCount)

                    val context = LocalContext.current
                    LaunchedEffect(key1 = notes.loadState, block = {
                        if (notes.loadState.refresh is LoadState.Error) {
                            Toast.makeText(context, "Error: ${(notes.loadState.refresh as LoadState.Error).error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                    if (notes.loadState.refresh is LoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        swipeRefreshState.isRefreshing = false
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(notes) { note ->
                                NoteItem(
                                    note = note,
                                    onNoteItemClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            key = Constants.ARG_NOTE,
                                            value = note
                                        )
                                        navController.navigate(Screen.NoteDetail.route)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: Note?, onNoteItemClick: (note: Note?) -> Unit) {
    val currentTime = System.currentTimeMillis()
    val title = note?.title ?: ""
    val content = note?.content ?: ""
    val status = note?.status ?: Status.TODO
    val deadline = (note?.deadline ?: Constants.INVALID_LONG_VALUE)
    val lastUpdate = (note?.updateTime ?: Constants.INVALID_LONG_VALUE).formatDateStyle1()
    val deadLineTag = if (deadline.formatDateStyle1() == currentTime.formatDateStyle1()) {
        DeadlineTag.TODAY
    } else if (deadline < currentTime) {
        DeadlineTag.OVERDUE
    } else {
        DeadlineTag.UPCOMING
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clickable {
                onNoteItemClick.invoke(note)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = when (deadLineTag) {
                        DeadlineTag.UPCOMING -> Color(0xffffffff)
                        DeadlineTag.OVERDUE -> Color(0xffe2e1e4)
                        DeadlineTag.TODAY -> Color(0xffe5ece4)
                    }
                )
                .padding(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.title_deadline, deadline.formatDateStyle1()),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
            )
            Text(
                text = stringResource(id = R.string.title_last_update, lastUpdate),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
            )
            Spacer(Modifier.height(10.dp))
            Row {
                Text(
                    modifier = Modifier
                        .background(
                            color = when (deadLineTag) {
                                DeadlineTag.UPCOMING -> Color(0xff9cd0ea)
                                DeadlineTag.OVERDUE -> Color(0xff9ea0a1)
                                DeadlineTag.TODAY -> Color(0xffe38a8b)
                            },
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 3.dp),
                    text = deadLineTag.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .background(
                            color = when (status) {
                                Status.TODO -> Color(0xffb8cae3)
                                Status.DONE -> Color(0xff82b99f)
                            }, shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 3.dp),
                    text = status.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun Toolbar(itemCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = stringResource(id = R.string.title_memory_notes))
        Text(
            text = itemCount.toString(), modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )

        Icon(
            modifier = Modifier
                .size(44.dp)
                .padding(10.dp),
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .size(44.dp)
                .padding(10.dp),
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun ToolbarPreview() {
//    NotesListScreen()
}