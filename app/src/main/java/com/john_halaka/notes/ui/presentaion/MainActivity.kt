package com.john_halaka.notes.ui.presentaion

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.john_halaka.notes.R
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.ui.presentaion.add_edit_note.components.AddEditNoteScreen
import com.john_halaka.notes.ui.presentaion.notes.NotesScreen
import com.john_halaka.notes.ui.presentaion.notes.components.NoteItem
import com.john_halaka.notes.ui.presentaion.search_notes.components.NotesSearchScreen
import com.john_halaka.notes.ui.Screen
import com.john_halaka.notes.ui.theme.NotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesTheme {
                // A surface container using the 'background' color from the theme

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(route = Screen.NotesSearchScreen.route) {
                            NotesSearchScreen (navController = navController)
                        }
                        composable(
                            route = Screen.AddEditNoteScreen.route +
                                    "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                              navArgument(
                                  name = "noteId"
                              ) {
                                  type = NavType.IntType
                                  defaultValue = -1
                              } ,
                                navArgument(
                                    name = "noteColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1

                            AddEditNoteScreen(navController = navController, noteColor = color)
                        }
                    }

                }

                }
            }
        }


