package com.john_halaka.notes.feature_note.presentaion.search_notes.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.Icon

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.notes.feature_note.domain.model.Note

import com.john_halaka.notes.feature_note.presentaion.notes.NotesEvent
import com.john_halaka.notes.feature_note.presentaion.notes.NotesViewModel
import com.john_halaka.notes.feature_note.presentaion.notes.components.ListViewNotes


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesSearchScreen (
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),


    ) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var notesList by remember { mutableStateOf<List<Note>>(state.notes) }
    var searchText by remember { mutableStateOf("") }


    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchPhrase ->
                        viewModel.onEvent(NotesEvent.SearchNotes(searchPhrase))
                        notesList = state.searchResult
                        searchText = searchPhrase
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    singleLine = true,
                    placeholder = { Text("Find in your notes") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 16.dp)
                                .clickable(
                                    true,
                                onClick = {
                                    viewModel.onEvent(NotesEvent.SearchNotes(searchText))
                                    notesList = state.searchResult
                                }
                                )
                        )
                    }
                )
            }

            ListViewNotes(

                navController = navController,
                viewModel = viewModel,
                scope = scope,
                snackbarHostState = snackbarHostState,
                notesList = notesList
            )


        }
    }
}
