package com.example.notescleanarchitecture.ui.feature.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.model.DeadlineTagFilter
import com.example.model.OptionItem
import com.example.model.StatusFilter
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.ui.feature.filter.component.ShowSelectDialog
import com.example.notescleanarchitecture.utils.MockData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NoteFilterScreen(navController: NavController, viewModel: NoteFilterViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true, block = {
        viewModel.getFilterSettings()
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is NoteFilterViewModel.NoteFilterUiState.SaveFilterDone -> {
                    navController.navigateUp()
                }
            }
        }
    })
    val context = LocalContext.current

    val deadlineTagState = viewModel.deadlineTagState.value
    val statusState = viewModel.statusState.value

    val deadLineTag = remember {
        mutableStateOf(OptionItem(displayName = context.getString(R.string.deadline_tag_all_type), value = DeadlineTagFilter.ALL))
    }
    val status = remember {
        mutableStateOf(OptionItem(displayName = context.getString(R.string.status_all_type), value = StatusFilter.ALL))
    }
    deadLineTag.value = MockData.deadlineTagFilterOptions(context).first {
        it.value == deadlineTagState
    }
    status.value = MockData.statusFilterOptions(context).first {
        it.value == statusState
    }
    val openDeadlineTagDialog = remember { mutableStateOf(false) }
    ShowSelectDialog(
        openDialog = openDeadlineTagDialog,
        title = stringResource(id = R.string.title_deadline_tag),
        listOption = MockData.deadlineTagFilterOptions(context),
        currentOption = deadLineTag,
        selectedOption = {
            viewModel.setDeadlineTag(it.value)
        }
    )

    val openStatusDialog = remember { mutableStateOf(false) }

    ShowSelectDialog(
        openDialog = openStatusDialog,
        title = stringResource(id = R.string.title_status),
        listOption = MockData.statusFilterOptions(context),
        currentOption = status,
        selectedOption = {
            viewModel.setStatus(it.value)
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = stringResource(id = R.string.title_deadline_tag))
        AssistChip(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            onClick = {
                openDeadlineTagDialog.value = true
            },
            label = { Text(deadLineTag.value.displayName) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Localized description",
                    Modifier.size(AssistChipDefaults.IconSize)
                )
            }
        )
        Text(
            text = stringResource(id = R.string.title_deadline_tag),
            modifier = Modifier.padding(top = 16.dp)
        )
        AssistChip(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            onClick = {
                openStatusDialog.value = true
            },
            label = { Text(status.value.displayName) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Localized description",
                    Modifier.size(AssistChipDefaults.IconSize)
                )
            }
        )

        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.End)
        ) {
            Text(text = stringResource(id = R.string.cancel_button))
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = stringResource(id = R.string.apply_button),
                modifier = Modifier.clickable {
                    viewModel.saveFilter(deadlineTagState, statusState)
                }
            )
        }
    }
}