package com.john_halaka.noteTopia.ui.presentaion.notes_list.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.john_halaka.noteTopia.ui.theme.Typography

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null
) {

    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }

    val transition = updateTransition(isFocused.value, label = "HeightTransition")
    val boxHeight by transition.animateDp(
        transitionSpec = {
            if (false isTransitioningTo true) {
                spring(stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessLow)
            }
        },
        label = "HeightAnimation"
    ) { state ->
        if (state) 48.dp else 36.dp
    }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp))
            .height(boxHeight)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused.value = it.isFocused },
        contentAlignment = Alignment.CenterStart
    ) {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = Typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSecondary),
            singleLine = true,
            maxLines = 1,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondary),
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    leadingIcon?.invoke()
                    Spacer(modifier = Modifier.width(8.dp))

                    if (value.isEmpty() && !isFocused.value) {
                        placeholder?.invoke()
                    }
                    innerTextField()
                }
            }
        )
    }
}