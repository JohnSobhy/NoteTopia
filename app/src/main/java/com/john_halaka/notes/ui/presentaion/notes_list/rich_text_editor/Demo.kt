package com.example.richtexteditor

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.input.input
import com.john_halaka.notes.R
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor


/**
Be sure that you have those two dependencies:
// Rich Text Editor
implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-beta03")

// Extension Icons
implementation("androidx.compose.material:material-icons-extended:1.5.3")
 */

fun Int.toComposeColor(): Color {
    return Color(
        android.graphics.Color.red(this),
        android.graphics.Color.green(this),
        android.graphics.Color.blue(this),
        android.graphics.Color.alpha(this)
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val state = rememberRichTextState()
    val titleSize = MaterialTheme.typography.displaySmall.fontSize
    val subtitleSize = MaterialTheme.typography.titleLarge.fontSize
    val context = LocalContext.current
    var selectedColor by remember { mutableStateOf(Color.Black) }

    val updateSelectedColor: (Color) -> Unit = { color ->
        selectedColor = color
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp)
                .padding(bottom = it.calculateBottomPadding())
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditorControls(
                modifier = Modifier.weight(2f),
                state = state,
                selectedColor = selectedColor,
                updateSelectedColor = updateSelectedColor,
                onBoldClick = {
                    state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                },
                onItalicClick = {
                    state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                },
                onUnderlineClick = {
                    state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                },
                onTitleClick = {
                    state.toggleSpanStyle(SpanStyle(fontSize = titleSize))
                },
                onSubtitleClick = {
                    state.toggleSpanStyle(SpanStyle(fontSize = subtitleSize))
                },

                onStartAlignClick = {
                    state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start))
                },
                onEndAlignClick = {
                    state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End))
                },
                onCenterAlignClick = {
                    state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
                },
                onColorClick = {
                    MaterialDialog(context).show {
                        colorChooser(
                            colors = ColorPalette.Primary,
                            allowCustomArgb = true
                        ) { _, color ->
                            val composeColor = color.toComposeColor()
                            state.toggleSpanStyle(SpanStyle(color = composeColor))
                            updateSelectedColor(composeColor)  // Update selectedColor

                        }
                        positiveButton(R.string.select)
                        negativeButton(R.string.cancel)
                    }
                },
                onLinkClick = {
                    var linkTitle: String = ""
                    var linkUrl: String = ""
                    MaterialDialog(context).show {
                        input(hintRes = R.string.enter_url) { _, text ->
                            linkUrl = text.toString()
                        }
                        MaterialDialog(context).show {
                            input(hintRes = R.string.enter_website_name) { _, text ->
                                linkTitle = text.toString()
                            }
                            state.addLink(text = linkTitle, url = linkUrl)
                            positiveButton(R.string.add)
                            negativeButton(R.string.cancel)
                        }
                    }
                },

                )
            RichTextEditor(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(8f),
                state = state,
            )
        }

    }
}

