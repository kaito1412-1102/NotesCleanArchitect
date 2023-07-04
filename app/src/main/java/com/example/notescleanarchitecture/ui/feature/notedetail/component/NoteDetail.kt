package com.example.notescleanarchitecture.ui.feature.notedetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.model.Status
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.extension.formatDateStyle1
import com.maxkeppeker.sheets.core.models.base.UseCaseState

@Composable
fun NoteDetail(
    status: Status,
    title: String,
    deadline: Long,
    calendarState: UseCaseState,
    content: String,
    statusChange: (Status) -> Unit,
    titleChange: (String) -> Unit,
    contentChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Toolbar(status) {
            statusChange.invoke(it)
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 20.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            value = title,
            onValueChange = {
                titleChange.invoke(it)
            },
            placeholder = {
                Text(text = stringResource(id = R.string.hint_note_title))
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = deadline.formatDateStyle1(),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                modifier = Modifier
                    .padding(6.dp)
                    .size(44.dp)
                    .clickable {
                        calendarState.show()
                    }
                    .padding(10.dp)
            )
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 20.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .weight(1f),
            value = content,
            onValueChange = {
                contentChange.invoke(it)
            },
            placeholder = {
                Text(text = stringResource(id = R.string.hint_note_content))
            }
        )
    }
}

@Composable
fun Toolbar(status: Status, onStatusChange: (status: Status) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.title_detail_note),
            modifier = Modifier.weight(1f)
        )

        Icon(
            modifier = Modifier
                .size(44.dp)
                .clickable {
                    val value = if (status == Status.TODO) Status.DONE else Status.TODO
                    onStatusChange.invoke(value)
                }
                .padding(10.dp),
            painter = painterResource(id = if (status == Status.TODO) R.drawable.ic_todo else R.drawable.ic_done),
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .size(44.dp)
                .padding(10.dp),
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = null
        )
    }
}