package com.john_halaka.noteTopia.ui.presentaion.notes_list.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.ui.Screen
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesEvent
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesViewModel
import com.john_halaka.noteTopia.ui.theme.LightGray
import com.john_halaka.noteTopia.ui.theme.MetallicGold


data class DropDownItem(
    val text: String,
    val icon: ImageVector
)

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier,
    isClickable: Boolean,
    navController: NavController,
    showFavoriteIcon: Boolean,
    showDefaultDropDownMenu: Boolean,
    viewModel: NotesViewModel,
    context: Context,
) {
    val (showDeleteNoteDialog, setShowDeleteNoteDialog) = remember { mutableStateOf(false) }

    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    var itemWidth by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val onFavoriteClick = {
        if (note.isFavourite)
            mToast(context, context.resources.getString(R.string.removed_from_favourites))
        else
            mToast(context, context.resources.getString(R.string.added_to_favorites))

        viewModel.onEvent(
            NotesEvent.UpdateNote(
                note.copy(
                    isFavourite = !note.isFavourite
                )
            )
        )
    }
    val defaultDropDownItems = mutableListOf<DropDownItem>()
    if (note.isPinned) {
        defaultDropDownItems.add(
            DropDownItem(
                stringResource(R.string.unpin),
                icon = Icons.Outlined.PushPin
            )
        )
    } else {
        defaultDropDownItems.add(
            DropDownItem(
                stringResource(R.string.pin),
                icon = Icons.Outlined.PushPin
            )
        )
    }
    defaultDropDownItems.add(
        DropDownItem(
            stringResource(R.string.delete),
            icon = Icons.Outlined.Delete
        )
    )

    val deletedNoteDropDownItems = listOf(
        DropDownItem(stringResource(R.string.restore), icon = Icons.Default.Refresh),
        DropDownItem(stringResource(R.string.delete_permanently), icon = Icons.Outlined.Delete)
    )

    val onClick = {
        if (isClickable) {
            navController.navigate(
                Screen.AddEditNoteScreen.route +
                        "?noteId=${note.id}&noteColor=${note.color}"
            )
        }
    }

    val onItemClick: (DropDownItem) -> Unit = { item ->
        if (showDefaultDropDownMenu) {
            when (item.text) {
                context.resources.getString(R.string.delete) -> {
                    viewModel.onEvent(
                        NotesEvent.MoveNoteToTrash(
                            note.copy(
                                isDeleted = !note.isDeleted
                            )
                        )
                    )
                    mToast(
                        context,
                        context.resources.getString(R.string.note_moved_to_trash)
                    )
                }

                context.resources.getString(R.string.pin) -> {
                    viewModel.onEvent(NotesEvent.PinNote(note))
                    mToast(context, context.resources.getString(R.string.note_pinned))
                }

                context.resources.getString(R.string.unpin) -> {
                    viewModel.onEvent(NotesEvent.UnpinNote(note))
                    mToast(context, context.resources.getString(R.string.note_unpinned))
                }
            }
        } else {
            if (item.text == context.resources.getString(R.string.restore)) {
                viewModel.onEvent(
                    NotesEvent.MoveNoteToTrash(
                        note.copy(
                            isDeleted = !note.isDeleted
                        )
                    )
                )
                mToast(context, context.resources.getString(R.string.note_restored))
            } else {
                setShowDeleteNoteDialog(true)
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(
                color = LightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
                itemWidth = with(density) { it.width.toDp() }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(true) {
                    detectTapGestures(
                        onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        },
                        onLongPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        },
                        onTap = {
                            onClick()
                        }

                    )
                }

        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.note_color_trans),
                        contentDescription = stringResource(R.string.note_color),
                        tint = Color(note.color)
                    )

                    if (note.isPinned && !note.isDeleted) {
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            modifier = Modifier.padding(top = 8.dp),
                            contentDescription = stringResource(R.string.pinned_note),
                            tint = MetallicGold
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 16.dp)
                ) {
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
                }
            }

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
                        contentDescription = stringResource(R.string.mark_note_as_favorite),
                        tint = Color.Unspecified,
                    )
                }
            }
        }

        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = {
                isContextMenuVisible = false
            },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight,
                x = pressOffset.x
            )


        ) {
            if (showDefaultDropDownMenu) {
                defaultDropDownItems.forEach { item ->
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(imageVector = item.icon, contentDescription = item.text)
                        },
                        text = {
                            Text(text = item.text)
                        },
                        onClick = {
                            onItemClick(item)
                            isContextMenuVisible = false
                        }
                    )

                }
            } else {
                deletedNoteDropDownItems.forEach { item ->
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(imageVector = item.icon, contentDescription = item.text)
                        },
                        text = {
                            Text(text = item.text)
                        },
                        onClick = {
                            onItemClick(item)
                            isContextMenuVisible = false
                        }
                    )

                }
            }
        }
    }


    if (showDeleteNoteDialog) {
        AlertDialog(
            onDismissRequest = { setShowDeleteNoteDialog(false) },
            title = { Text(stringResource(R.string.warning)) },
            text = { Text("This note will be removed permanently!") },
            confirmButton = {
                Button(onClick = {
                    viewModel.onEvent(NotesEvent.DeleteNote(note))
                    setShowDeleteNoteDialog(false)
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                Button(onClick = { setShowDeleteNoteDialog(false) }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

fun mToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

