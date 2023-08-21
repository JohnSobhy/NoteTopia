package com.john_halaka.notes.ui.presentaion.deleted_notes


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.ui.presentaion.notes_list.components.NoteItem

@Composable
fun DeletedNoteItem(
    note: Note,
    onRestoreClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        NoteItem(note = note,
            onFavoriteClick = {},
            modifier = Modifier
                .clickable(enabled = false) {}
                .weight(1f)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextButton(
                onClick =
                onRestoreClick
            ) {
                Text(text = "Restore")
            }
            Spacer(modifier = Modifier.height(4.dp))
            TextButton(
                onClick =
                onRemoveClick
            ) {
                Text(text = "Remove")
            }
        }
    }
}