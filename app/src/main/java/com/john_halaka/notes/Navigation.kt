package com.john_halaka.notes

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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

    }
}

@Composable
fun NavigationDrawer(
    navController: NavController,
    drawerState: DrawerState,
    content: @Composable () -> Unit,
    scope: CoroutineScope
) {
    val currentRoute = navController.currentDestination?.route
    val itemList: List<NavigationItem> = listOf(
        NavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = Screen.NotesScreen.route
        ),
        NavigationItem(
            title = "Recently Deleted",
            selectedIcon = Icons.Filled.Delete,
            unselectedIcon = Icons.Outlined.Delete,
            route = Screen.DeletedNotesScreen.route
        )
    )
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                itemList.forEach { navigationItem ->
                    NavigationDrawerItem(
                        label = { Text(text = navigationItem.title) },
                        selected = currentRoute == navigationItem.route,
                        onClick = {
                            navController.navigate(navigationItem.route)
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (currentRoute == navigationItem.route) {
                                    navigationItem.selectedIcon
                                } else
                                    navigationItem.unselectedIcon,
                                contentDescription = navigationItem.title
                            )
                        },
                        //   badge = {},
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                            .width(intrinsicSize = IntrinsicSize.Min)

                    )
                }

            }
        },
        drawerState = drawerState,
        content = content
    )
}

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)