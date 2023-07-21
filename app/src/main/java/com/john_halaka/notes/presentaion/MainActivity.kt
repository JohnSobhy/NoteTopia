package com.john_halaka.notes.presentaion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import com.john_halaka.notes.R
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.presentaion.notes.components.NoteItem
import com.john_halaka.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesTheme {
                // A surface container using the 'background' color from the theme

                NoteItem(note = Note(1, "Test", "TestContent", color = (R.color.teal_200), timestamp = 1L ), modifier = Modifier) {

                }
                }
            }
        }
    }


