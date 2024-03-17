package com.john_halaka.noteTopia.ui.presentaion.notes_list.components

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesViewModel

@Composable
fun NoteItemCommon(
    note: Note,
    context: Context,
    viewModel: NotesViewModel,
    modifier: Modifier,
    showFavoriteIcon: Boolean,
    isClickable: Boolean,
    showDefaultDropDownMenu: Boolean,
    navController: NavController
) {
    NoteItem(
        note = note,
        context = context,
        viewModel = viewModel,
        modifier = modifier
            .fillMaxWidth(),
        showFavoriteIcon = showFavoriteIcon,
        showDefaultDropDownMenu = showDefaultDropDownMenu,
        isClickable = isClickable,
        navController = navController
    )
}

@Composable
fun NoteGrid(
    navController: NavController,
    viewModel: NotesViewModel,
    notesList: List<Note>,
    context: Context,
    showFavoriteIcon: Boolean,
    isClickable: Boolean,
    showDefaultDropDownMenu: Boolean,
    columns: Int,
    cellHeightDpValue: Dp
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.padding(8.dp)
    ) {
        items(notesList) { note ->
            NoteItemCommon(
                note = note,
                context = context,
                viewModel = viewModel,
                modifier = Modifier
                    .height(cellHeightDpValue),
                showFavoriteIcon = showFavoriteIcon,
                navController = navController,
                isClickable = isClickable,
                showDefaultDropDownMenu = showDefaultDropDownMenu
            )
        }
    }
}

@Composable
fun GridViewNotes(
    navController: NavController,
    viewModel: NotesViewModel,
    notesList: List<Note>,
    context: Context,
    showFavoriteIcon: Boolean,
) {
    NoteGrid(
        navController,
        viewModel,
        notesList,
        context,
        showFavoriteIcon,
        isClickable = true,
        showDefaultDropDownMenu = true,
        columns = 2,
        cellHeightDpValue = 140.dp
    )
}

@Composable
fun SmallGridViewNotes(
    navController: NavController,
    viewModel: NotesViewModel,
    notesList: List<Note>,
    context: Context,
    showFavoriteIcon: Boolean,
) {
    NoteGrid(
        navController,
        viewModel,
        notesList,
        context,
        showFavoriteIcon,
        isClickable = true,
        showDefaultDropDownMenu = true,
        3, 120.dp
    )
}

@Composable
fun ListViewNotes(
    navController: NavController,
    viewModel: NotesViewModel,
    notesList: List<Note>,
    context: Context,
    showFavoriteIcon: Boolean,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        items(notesList) { note ->
            NoteItemCommon(
                note = note,
                context = context,
                viewModel = viewModel,
                modifier = Modifier
                    .height(140.dp),
                showFavoriteIcon = showFavoriteIcon,
                navController = navController,
                isClickable = true,
                showDefaultDropDownMenu = true
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
