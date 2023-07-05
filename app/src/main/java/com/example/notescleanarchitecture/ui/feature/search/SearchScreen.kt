package com.example.notescleanarchitecture.ui.feature.search

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.model.Note
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.navigation.Screen
import com.example.notescleanarchitecture.ui.feature.NoteViewModel
import com.example.notescleanarchitecture.ui.feature.notelist.NoteItem
import com.example.notescleanarchitecture.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: NoteViewModel = hiltViewModel()) {
    val searchText = remember {
        mutableStateOf("")
    }
    val active = remember {
        mutableStateOf(false)
    }

    val itemsNote = remember {
        mutableStateOf(viewModel.searchNotes(""))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        /*       TextField(
                   modifier = Modifier.fillMaxWidth(),
                   value = searchText.value,
                   onValueChange = { value ->
                       searchText.value = value
                   },
                   placeholder = {
                       Text(text = stringResource(id = R.string.hint_search_note))
                   })*/

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            query = searchText.value,
            onQueryChange = {
                searchText.value = it
            },
            onSearch = {
                active.value = false
                itemsNote.value = viewModel.searchNotes(searchText.value)
            },
            active = active.value,
            onActiveChange = {
                active.value = it
            },
            placeholder = {
                Text(text = stringResource(id = R.string.hint_search_note))
            },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = null)
            },
            trailingIcon = {
                if (active.value) {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                if (searchText.value.isNotEmpty()) {
                                    searchText.value = ""
                                } else {
                                    active.value = false
                                }
                            }
                            .size(44.dp)
                            .padding(10.dp),
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null
                    )
                }
            }

        ) {

        }
        val notes = itemsNote.value.collectAsLazyPagingItems()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(notes) { note ->
                Log.d("tuanminh", "SearchScreen: $note")
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