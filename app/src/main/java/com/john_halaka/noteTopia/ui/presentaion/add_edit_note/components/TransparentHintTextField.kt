package com.john_halaka.noteTopia.ui.presentaion.add_edit_note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit
) {
    Box(modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)) {

        val customTextSelectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.onPrimary,
            backgroundColor = MaterialTheme.colorScheme.onTertiary
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {

            SelectionContainer {
                BasicTextField(
                    value = text,
                    onValueChange = onValueChange,
                    singleLine = singleLine,
                    textStyle = textStyle,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            onFocusChange(it)
                        }
                )
            }

            if (isHintVisible) {
                Text(
                    text = hint,
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

//                // Convert the text to AnnotatedString and make links clickable
//                val annotatedString = buildAnnotatedString {
//                    val urlPattern = Regex("https?://\\S+|www\\.\\S+")
//
//                    urlPattern.findAll(text).forEach { result ->
//                        val start = result.range.first
//                        val end = result.range.last + 1
//
//                        // Add a custom annotation for each URL
//                        addStringAnnotation("URL", result.value, start, end)
//                    }
//                }
//
//                BasicText(
//                    text = annotatedString,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .pointerInput(Unit) {
//                            detectTapGestures { offset ->
//                                // Retrieve the annotation at the clicked offset
//                                annotatedString.getStringAnnotations("URL", offset.x.toInt(), offset.y.toInt())
//                                    .firstOrNull()?.let { annotation ->
//                                        // Handle the URL click (e.g., open a web browser)
//                                        // You can replace this with your custom logic
//                                        openUrl(annotation.item)
//                                    }
//                            }
//                        }
//                )
//            }
//        }
//    }
//}
//
//// Replace this function with your logic to open a URL
//fun openUrl(url: String) {
//    // Implement your logic to open the URL (e.g., use an Intent or launch a browser)
//    // ...
//}