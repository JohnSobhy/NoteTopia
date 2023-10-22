package com.john_halaka.notes.ui.presentaion.fav_notes

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.notes.BottomNavigationBar
import com.john_halaka.notes.R
import com.john_halaka.notes.ui.presentaion.notes_list.NotesEvent
import com.john_halaka.notes.ui.presentaion.notes_list.NotesViewModel
import com.john_halaka.notes.ui.presentaion.notes_list.components.ListViewNotes
import com.john_halaka.notes.ui.presentaion.notes_list.components.OrderSection
import com.john_halaka.notes.ui.theme.BabyBlue
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavNotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
    context: Context
) {
    val state = viewModel.state.value
    Log.d("favNotesScreen", "state: $state")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val notesList = state.favouriteNotes
   // val enableCheckBox = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Favorites")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    })
                    {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(NotesEvent.ToggleOrderSection)
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_sort_24),
                            contentDescription = "Sort notes"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },

        ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)

        ) {


            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))

                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            if (state.favouriteNotes.isEmpty()) {
                var showProgress by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(1000)
                    showProgress = false
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (showProgress) {
                        CircularProgressIndicator(color = BabyBlue)
                    } else
                        Text(text = "You have no favorite notes")
                }
            } else {
                ListViewNotes(
                    navController = navController,
                    viewModel = viewModel,
                    scope = scope,
                    snackbarHostState = snackbarHostState,
                    notesList = notesList,
                    context = context,
                    showFavoriteIcon = true,
                    //   showCheckBox = enableCheckBox.value
                )
            }
        }
    }
}





