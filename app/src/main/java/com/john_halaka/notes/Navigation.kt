package com.john_halaka.notes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.john_halaka.mytodo.AddEditTodoScreen
import com.john_halaka.mytodo.TodoListScreen
import com.john_halaka.notes.ui.Screen
import com.john_halaka.notes.ui.presentaion.add_edit_note.components.AddEditNoteScreen
import com.john_halaka.notes.ui.presentaion.deleted_notes.DeletedNotesScreen
import com.john_halaka.notes.ui.presentaion.fav_notes.FavNotesScreen
import com.john_halaka.notes.ui.presentaion.notes_list.NotesScreen
import com.john_halaka.notes.ui.presentaion.search_notes.components.NotesSearchScreen


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.NotesScreen.route
    ) {
        composable(
            route = Screen.NotesScreen.route
        ) {
            NotesScreen(navController = navController, context = LocalContext.current)
        }

        composable(route = Screen.FavNotesScreen.route) {
            FavNotesScreen(navController = navController, context = LocalContext.current)
        }
        composable(route = Screen.DeletedNotesScreen.route) {
            DeletedNotesScreen(navController = navController, context = LocalContext.current)
        }

        composable(route = Screen.NotesSearchScreen.route) {
            NotesSearchScreen(navController = navController, context = LocalContext.current)
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
                },
                navArgument(
                    name = "noteColor"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            val color = it.arguments?.getInt("noteColor") ?: -1

            AddEditNoteScreen(
                navController = navController,
                noteColor = color,
                context = LocalContext.current
            )
        }

        composable(route = Screen.TodoListScreen.route) {
            TodoListScreen(
                onNavigate = {
                    navController.navigate(it.route)
                },
                navController = navController
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

@Composable
fun BottomNavigationBar(
    navController: NavController,

    ) {

    val currentRoute = navController.currentDestination?.route
    val itemList: List<NavigationItem> = listOf(
        NavigationItem(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            route = Screen.NotesSearchScreen.route
        ),
        NavigationItem(
            title = "Notes",
            selectedIcon = ImageVector.vectorResource(R.drawable.notes_svg),
            unselectedIcon = ImageVector.vectorResource(R.drawable.notes_svg),
            route = Screen.NotesScreen.route
        ),
        NavigationItem(
            title = "Add",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add,
            route = if (currentRoute == Screen.TodoListScreen.route) {
                Screen.AddEditTodoScreen.route
            } else {
                Screen.AddEditNoteScreen.route
            }.toString()


        ),
        NavigationItem(
            title = "Todo List",
            selectedIcon = ImageVector.vectorResource(R.drawable.check_lists),
            unselectedIcon = ImageVector.vectorResource(R.drawable.check_lists),
            route = Screen.TodoListScreen.route
        ),
        NavigationItem(
            title = "Favorites",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.Favorite,
            route = Screen.FavNotesScreen.route
        )
    )

    NavigationBar {
        itemList.forEach { navigationItem ->
            NavigationBarItem(
                selected = currentRoute == navigationItem.route,
                onClick = {
                    navController.navigate(navigationItem.route)
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == navigationItem.route) {
                            navigationItem.selectedIcon
                        } else navigationItem.unselectedIcon,
                        contentDescription = navigationItem.title
                    )
                },
                label = {
                    Text(text = navigationItem.title)
                }
            )
        }


//        NavigationBarItem(
//            modifier = Modifier
//                .padding(bottom = 16.dp)
//                .shadow(elevation = 4.dp),
//            selected = false,
//            onClick = {
//                navController.navigate(Screen.AddEditNoteScreen.route)
//            },
//            icon = {
//                Icon(
//                    imageVector = Icons.Rounded.Add,
//                    contentDescription = "Add Note",
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(top = 12.dp)
//                )
//            },
//
//
//            )
//


    }
}

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)