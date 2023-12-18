package com.john_halaka.noteTopia.ui.presentaion.add_edit_note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.john_halaka.noteTopia.feature_note.domain.model.Note


@Composable
fun ColorPicker(
    noteColor: Color,
    onColorChange: (ColorEnvelope) -> Unit,
) {
    val colorPickerController = rememberColorPickerController()

    Box(
        modifier = Modifier
            .width(300.dp)
            .padding(16.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            // color picker
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp),
                controller = colorPickerController,
                initialColor = noteColor,
                onColorChanged = onColorChange
            )
            // transparency
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AlphaSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .height(20.dp),
                    controller = colorPickerController,
                )
            }
            // brightness
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .height(20.dp),
                controller = colorPickerController,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // color tile
                AlphaTile(
                    modifier = Modifier
                        .height(32.dp)
                        .width(50.dp),
                    controller = colorPickerController
                )
                // save color
                IconButton(
                    modifier = Modifier.width(50.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(imageVector = Icons.Filled.SaveAlt, contentDescription = "save color")
                }
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // saved colors
                items(Note.noteColors) { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .shadow(4.dp, RectangleShape)
                            .clip(RoundedCornerShape(10))
                            .background(color)
                            .border(
                                width = 2.dp,
                                color = if (noteColor.toArgb() == colorInt) {
                                    MaterialTheme.colorScheme.onSurface
                                } else Color.Transparent,
                                shape = RoundedCornerShape(10)
                            )
                            .clickable {

                            }

                    )
                }

            }
        }
    }

}