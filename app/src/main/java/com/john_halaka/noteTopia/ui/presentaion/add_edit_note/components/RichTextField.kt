package com.john_halaka.noteTopia.ui.presentaion.add_edit_note.components

import androidx.compose.runtime.Composable
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@Composable
fun RichTextField() {
    val state = rememberRichTextState()

    RichTextEditor(state = state)
}