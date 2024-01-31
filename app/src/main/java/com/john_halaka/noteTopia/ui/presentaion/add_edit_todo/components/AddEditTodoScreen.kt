package com.john_halaka.mytodo

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.john_halaka.mytodo.ui.add_edit_todo.AddEditTodoEvent
import com.john_halaka.mytodo.ui.add_edit_todo.AddEditTodoViewModel
import com.john_halaka.noteTopia.R


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditTodoScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {
    val scaffoldState = remember {
        SnackbarHostState()
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddEditTodoViewModel.UiEvent.PopBackStack -> onPopBackStack()
                is AddEditTodoViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.showSnackbar(
                        message = context.getString(event.messageResId),
                        actionLabel = event.actionResId?.let { context.getString(it) }
                    )
                }

                else -> Unit
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(scaffoldState)
        },
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditTodoEvent.OnSaveTodoClicked)
                },
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.save_todo)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val customTextSelectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.onTertiary
            )

            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                SelectionContainer {
                    TextField(
                        value = viewModel.title,
                        onValueChange = {
                            viewModel.onEvent(AddEditTodoEvent.OnTitleChanged(it))
                        },
                        placeholder = {
                            Text(text = stringResource(R.string.title))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        shape = RoundedCornerShape(10),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            //backgroundColor = Color.LightGray
                            disabledIndicatorColor = Color.Transparent,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                SelectionContainer {
                    TextField(
                        value = viewModel.description,
                        onValueChange = {
                            viewModel.onEvent(AddEditTodoEvent.OnDescriptionChanged(it))
                        },
                        placeholder = {
                            Text(text = stringResource(R.string.description_optional))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        shape = RoundedCornerShape(10),
                        singleLine = false,
                        maxLines = 5,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            //backgroundColor = Color.LightGray
                            disabledIndicatorColor = Color.Transparent,
                        )

                    )
                }

            }
        }
    }
}
//
//@Composable
//fun MySnackbar(
//    snackbarHostState: SnackbarHostState,
//    modifier: Modifier = Modifier,
//    durationMillis: Long = 5000,  // Duration in milliseconds
//) {
//    LaunchedEffect(key1 = snackbarHostState.currentSnackbarData) {
//        delay(durationMillis)
//        snackbarHostState.currentSnackbarData?.dismiss()
//    }
//
//    SnackbarHost(
//        hostState = snackbarHostState,
//        modifier = modifier
//    )
//}


@Preview
@Composable
fun AddEditTodoPreview() {
    AddEditTodoScreen(onPopBackStack = { })
}
