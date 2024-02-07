package com.john_halaka.noteTopia.ui.presentaion.search_notes.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesEvent
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesViewModel
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.ListViewNotes
import com.john_halaka.noteTopia.ui.theme.Typography


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesSearchScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
    context: Context

) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var notesList by remember { mutableStateOf(state.notes) }
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                ),
                textStyle = Typography.titleLarge,
                singleLine = true,
                placeholder = {
                    Text(
                        stringResource(R.string.find_in_your_notes),
                        style = Typography.titleLarge,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier
                            .clickable(
                                true,
                                onClick = {
                                    navController.navigateUp()
                                }
                            )

                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon),
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
//            scope = scope,
//            snackbarHostState = snackbarHostState,
            notesList = notesList,
            context = context,
            showFavoriteIcon = true
        )


    }
}
