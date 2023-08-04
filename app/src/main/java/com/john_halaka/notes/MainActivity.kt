package com.john_halaka.notes

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.john_halaka.mytodo.AddEditTodoScreen
import com.john_halaka.mytodo.TodoListScreen
import com.john_halaka.notes.ui.presentaion.add_edit_note.components.AddEditNoteScreen
import com.john_halaka.notes.ui.presentaion.notes_list.NotesScreen
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

                        composable(route = Screen.TodoListScreen.route) {
                            TodoListScreen(
                                onNavigate = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                        composable(
                            route = Screen.AddEditTodoScreen.route + "?todoId={todoId}",
                            arguments = listOf(
                                navArgument(name = "todoId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            AddEditTodoScreen(onPopBackStack = { navController.popBackStack() })

                        }
                    }

                }

                }
            }
        }


