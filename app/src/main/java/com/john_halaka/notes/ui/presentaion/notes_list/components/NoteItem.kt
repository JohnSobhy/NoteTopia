package com.john_halaka.notes.ui.presentaion.notes_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.john_halaka.notes.R
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.ui.theme.LightGray

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier,
    cornerRadius: Dp = 10.dp,
    onFavoriteClick: () -> Unit,
    showFavoriteIcon: Boolean,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(
                color = LightGray,
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.note_color_trans),
                    contentDescription = "note color",
                    tint = Color(note.color)
                )
            }
            Text(
                text = note.title,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.content,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End,
            ) {

            }

        }
//        Checkbox(
//            modifier = Modifier
//                .align(Alignment.CenterEnd)
//                .padding(end = 4.dp),
//            checked = false,
//            colors = CheckboxDefaults.colors(
//                checkmarkColor = BrandGreen,
//                disabledCheckedColor = Color.Transparent
//            ),
//            enabled = false,
//            onCheckedChange = null // We handle the state in the combinedClickable
//        )
        if (showFavoriteIcon) {
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = if (note.isFavourite)
                        ImageVector.vectorResource(R.drawable.fav_note_selected)
                    else ImageVector.vectorResource(R.drawable.fav_note_unselected),
                    contentDescription = "Favorite note",
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

