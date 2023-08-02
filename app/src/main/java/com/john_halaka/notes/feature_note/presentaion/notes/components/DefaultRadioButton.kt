package com.john_halaka.notes.feature_note.presentaion.notes.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DefaultRadioButton (
    text: String,
    selected: Boolean,
    onSelect: ()-> Unit,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
       RadioButton(
           selected = selected,
           onClick = onSelect,
           colors = RadioButtonDefaults.colors(
               selectedColor = MaterialTheme.colorScheme.primary,
               unselectedColor = MaterialTheme.colorScheme.onBackground
           )
       )

        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

@Preview
@Composable
fun DefaultRadioButtonPreview() {
    DefaultRadioButton("title", true, onSelect = { TODO() })
}