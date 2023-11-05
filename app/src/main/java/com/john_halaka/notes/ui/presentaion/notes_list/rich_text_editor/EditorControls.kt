package com.example.richtexteditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditorControls(
    modifier: Modifier = Modifier,
    state: RichTextState,
    onBoldClick: () -> Unit,
    onItalicClick: () -> Unit,
    onUnderlineClick: () -> Unit,
    onTitleClick: () -> Unit,
    onSubtitleClick: () -> Unit,
    onColorClick: (Color) -> Unit,
    onStartAlignClick: () -> Unit,
    onEndAlignClick: () -> Unit,
    onCenterAlignClick: () -> Unit,
    selectedColor: Color,
    updateSelectedColor: (Color) -> Unit,
    // onExportClick: () -> Unit,
    onLinkClick: () -> Unit,

    ) {
    val titleSize = MaterialTheme.typography.displaySmall.fontSize
    val subtitleSize = MaterialTheme.typography.titleLarge.fontSize

    var boldSelected by rememberSaveable {
        mutableStateOf(
            state.currentSpanStyle.fontWeight == FontWeight.Bold
        )
    }
    var italicSelected by rememberSaveable {
        mutableStateOf(
            state.currentSpanStyle.fontStyle == FontStyle.Italic
        )
    }
    var underlineSelected by rememberSaveable {
        mutableStateOf(
            state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) ?: false
        )
    }
    var titleSelected by rememberSaveable {
        mutableStateOf(
            state.currentSpanStyle.fontSize == titleSize
        )
    }
    var subtitleSelected by rememberSaveable {
        mutableStateOf(
            state.currentSpanStyle.fontSize == subtitleSize
        )
    }

    var startAlignSelected by rememberSaveable {
        mutableStateOf(
            state.currentParagraphStyle.textAlign == TextAlign.Start
        )
    }
    var endAlignSelected by rememberSaveable {
        mutableStateOf(
            state.currentParagraphStyle.textAlign == TextAlign.End
        )
    }
    var centerAlignSelected by rememberSaveable {
        mutableStateOf(
            state.currentParagraphStyle.textAlign == TextAlign.Center
        )
    }
    var colorPickerSelected by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(state.currentSpanStyle) {
        boldSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold
        italicSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic
        underlineSelected =
            state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) ?: false
        titleSelected = state.currentSpanStyle.fontSize == titleSize
        subtitleSelected = state.currentSpanStyle.fontSize == subtitleSize
    }

    LaunchedEffect(state.currentParagraphStyle) {
        startAlignSelected = state.currentParagraphStyle.textAlign == TextAlign.Start
        endAlignSelected = state.currentParagraphStyle.textAlign == TextAlign.End
        centerAlignSelected = state.currentParagraphStyle.textAlign == TextAlign.Center
    }

    LaunchedEffect(state.selection) {
        boldSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold
        italicSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic
        underlineSelected =
            state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) ?: false
        titleSelected = state.currentSpanStyle.fontSize == titleSize
        subtitleSelected = state.currentSpanStyle.fontSize == subtitleSize
        startAlignSelected = state.currentParagraphStyle.textAlign == TextAlign.Start
        endAlignSelected = state.currentParagraphStyle.textAlign == TextAlign.End
        centerAlignSelected = state.currentParagraphStyle.textAlign == TextAlign.Center
    }

    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ControlWrapper(
            selected = boldSelected,
            onChangeClick = { boldSelected = it },
            onClick = onBoldClick
        ) {
            Icon(
                imageVector = Icons.Default.FormatBold,
                contentDescription = "Bold Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = italicSelected,
            onChangeClick = { italicSelected = it },
            onClick = onItalicClick
        ) {
            Icon(
                imageVector = Icons.Default.FormatItalic,
                contentDescription = "Italic Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = underlineSelected,
            onChangeClick = { underlineSelected = it },
            onClick = onUnderlineClick
        ) {
            Icon(
                imageVector = Icons.Default.FormatUnderlined,
                contentDescription = "Underline Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = titleSelected,
            onChangeClick = { titleSelected = it },
            onClick = onTitleClick
        ) {
            Icon(
                imageVector = Icons.Default.Title,
                contentDescription = "Title Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = subtitleSelected,
            onChangeClick = { subtitleSelected = it },
            onClick = onSubtitleClick
        ) {
            Icon(
                imageVector = Icons.Default.FormatSize,
                contentDescription = "Subtitle Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = colorPickerSelected,
            onChangeClick = { colorPickerSelected = it },
            onClick = { onColorClick(selectedColor) }
        ) {
            Icon(
                imageVector = Icons.Default.FormatColorText,
                contentDescription = "Color Control",
                tint = selectedColor
            )
        }


        ControlWrapper(
            selected = startAlignSelected,
            onChangeClick = { startAlignSelected = !startAlignSelected },
            onClick = onStartAlignClick
        ) {
            Icon(
                imageVector = Icons.Default.FormatAlignLeft,
                contentDescription = "Start Align Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = centerAlignSelected,
            onChangeClick = { centerAlignSelected = !centerAlignSelected },
            onClick = onCenterAlignClick
        ) {
            Icon(
                imageVector = Icons.Default.FormatAlignCenter,
                contentDescription = "Center Align Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = endAlignSelected,
            onChangeClick = { endAlignSelected = !endAlignSelected },
            onClick = onEndAlignClick
        ) {
            Icon(
                imageVector = Icons.Default.FormatAlignRight,
                contentDescription = "End Align Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

    }
}