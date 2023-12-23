package com.john_halaka.noteTopia.ui.presentaion.add_edit_note.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note_color.domain.model.NoteColor
import com.john_halaka.noteTopia.ui.presentaion.add_edit_note.AddEditNoteEvent
import com.john_halaka.noteTopia.ui.presentaion.add_edit_note.AddEditNoteViewModel

@Composable
fun ColorPicker(
    onColorChange: (ColorEnvelope) -> Unit,
    viewModel: AddEditNoteViewModel,
    onCancelClick: (() -> Unit),
    onDoneClick: (() -> Unit)
) {
    val colorPickerController = rememberColorPickerController()
    val savedNoteColors by viewModel.savedNoteColors.observeAsState(emptyList())
    val (showDeleteAllDialog, setShowDeleteAllDialog) = remember { mutableStateOf(false) }
    val (showDeleteColorDialog, setShowDeleteColorDialog) = remember { mutableStateOf(false) }
    val (colorToDelete, setColorToDelete) = remember { mutableStateOf<NoteColor?>(null) }

    val initialColor =
        Color(viewModel.noteColor.value)
    Log.d("Initial color", "Initial color is $initialColor")


    // Local functions that do something in this composable
    fun localOnCancelClick() {
        viewModel.cancelClicked()
    }

    fun localOnDoneClick() {
        viewModel.doneClicked()
        Log.d("controllerColor", "${colorPickerController.selectedColor.value}")
    }

    // Wrapper functions that call both the local function and the function from the constructor
    fun wrappedOnCancelClick() {
        localOnCancelClick()
        onCancelClick()
    }

    fun wrappedOnDoneClick() {
        localOnDoneClick()
        onDoneClick()
    }


    Box(
        modifier = Modifier
            .width(300.dp)
            .height(440.dp)
            .background(
                MaterialTheme.colorScheme.onSecondary,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Top
                    ) {
                        // saved colors
                        items(Note.noteColors) { color ->
                            val colorInt = color.toArgb()
                            Box(
                                modifier = Modifier
                                    //.size(25.dp)
                                    .height(50.dp)
                                    .width(40.dp)
                                    .padding(end = 4.dp, top = 4.dp, bottom = 4.dp)
                                    .clip(RoundedCornerShape(10))
                                    .background(color)
                                    .border(
                                        width = 2.dp,
                                        color = if (viewModel.tempNoteColor.value == colorInt) {
                                            MaterialTheme.colorScheme.onSurface
                                        } else Color.Transparent,
                                        shape = RoundedCornerShape(10)
                                    )
                                    .clickable {
                                        viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                        Log.d("new initial color ", "is $initialColor")
                                    }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(4f)
                ) {
                    // color picker
                    HsvColorPicker(
                        initialColor = initialColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp),
                        controller = colorPickerController,
                        onColorChanged = onColorChange
                    )
                    // transparency
                    AlphaSlider(
                        initialColor = initialColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 8.dp)
                            .height(15.dp),
                        controller = colorPickerController,
                    )
                    // brightness
                    BrightnessSlider(
                        initialColor = initialColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 8.dp)
                            .height(15.dp),
                        controller = colorPickerController,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, start = 4.dp, end = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //hex value
                        //maybe, a regular text field,
                        // the user will enter a hex value not int so we need a function that converts hex values to int
                        // then we can pass that int to
                        //onValueChang{tempColor.value = value}

                        // color tile
                        AlphaTile(
                            modifier = Modifier
                                .height(40.dp)
                                .width(50.dp),
                            controller = colorPickerController
                        )
                        // save color
                        IconButton(
                            modifier = Modifier
                                .width(50.dp)
                                .height(40.dp),
                            onClick = {
                                viewModel.addNoteColor(colorPickerController.selectedColor.value.toArgb())
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SaveAlt,
                                contentDescription = stringResource(R.string.save_color)
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp)
            ) {
                LazyRow(
                    modifier = Modifier
                        .weight(7f)
                        .padding(start = 8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    items(savedNoteColors.reversed()) { color ->
                        val colorInt: Int = color.argb
                        Box(
                            modifier = Modifier
                                //.size(25.dp)
                                .height(50.dp)
                                .width(50.dp)
                                .padding(end = 8.dp)
                                .clip(RoundedCornerShape(10))
                                .background(Color(colorInt))
                                .border(
                                    width = 2.dp,
                                    color = if (viewModel.tempNoteColor.value == colorInt) {
                                        MaterialTheme.colorScheme.onSurface
                                    } else Color.Transparent,
                                    shape = RoundedCornerShape(10)
                                )
                                .clickable {
                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                },

                            ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(15.dp)
                                    .clickable {
                                        setColorToDelete(color)
                                        // warning before deleting note
                                        setShowDeleteColorDialog(true)
                                    },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = stringResource(R.string.delete_color)
                                )
                            }
                        }
                    }
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        // show a warning msg that all the saved colors will be deleted
                        setShowDeleteAllDialog(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = stringResource(R.string.delete_saved_colors)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { wrappedOnDoneClick() }

                ) {
                    Text(stringResource(R.string.done))
                }
                Button(
                    onClick = { wrappedOnCancelClick() }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }

    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { setShowDeleteAllDialog(false) },
            title = { Text(stringResource(R.string.warning)) },
            text = { Text(stringResource(R.string.delete_all_colors_permanently)) },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteAllNoteColors()
                    setShowDeleteAllDialog(false)
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                Button(onClick = { setShowDeleteAllDialog(false) }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    if (showDeleteColorDialog) {
        AlertDialog(
            onDismissRequest = { setShowDeleteColorDialog(false) },
            title = { Text(stringResource(R.string.warning)) },
            text = { Text(stringResource(R.string.delete_this_color)) },
            confirmButton = {
                Button(onClick = {
                    colorToDelete?.let { viewModel.deleteNoteColor(it) }
                    setColorToDelete(null)
                    setShowDeleteColorDialog(false)
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                Button(onClick = {
                    setColorToDelete(null)
                    setShowDeleteColorDialog(false)
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }


}

