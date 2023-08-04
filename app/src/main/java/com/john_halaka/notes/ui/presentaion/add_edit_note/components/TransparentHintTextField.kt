package com.john_halaka.notes.ui.presentaion.add_edit_note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun TransparentHintTextField (
    text: String,
    hint : String,
    modifier : Modifier = Modifier,
    isHintVisible : Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),

    singleLine : Boolean = false,
    onFocusChange: (FocusState) -> Unit
) {
    Box(modifier = modifier.padding(16.dp)) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimary) ,
            modifier = Modifier
                .fillMaxWidth()

                .onFocusChanged {
                    onFocusChange(it)
                }
        )

        if (isHintVisible) {
            Text(text = hint, style = textStyle, color = MaterialTheme.colorScheme.onBackground)

        }
    }
}