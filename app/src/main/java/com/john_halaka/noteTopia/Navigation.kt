package com.john_halaka.noteTopia

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.john_halaka.mytodo.AddEditTodoScreen
import com.john_halaka.mytodo.TodoListScreen
import com.john_halaka.noteTopia.ui.Screen
import com.john_halaka.noteTopia.ui.presentaion.add_edit_note.components.AddEditNoteScreen
import com.john_halaka.noteTopia.ui.presentaion.daily_quote.DailyQuote
import com.john_halaka.noteTopia.ui.presentaion.deleted_notes.DeletedNotesScreen
import com.john_halaka.noteTopia.ui.presentaion.fav_notes.FavNotesScreen
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesScreen
import com.john_halaka.noteTopia.ui.presentaion.search_notes.components.NotesSearchScreen
import com.john_halaka.noteTopia.ui.presentaion.util.findActivity
import com.john_halaka.noteTopia.ui.theme.BrandGreen
import com.john_halaka.noteTopia.ui.theme.IconColorGray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Navigation() {
    // val systemUiController = rememberSystemUiController()
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

        composable(
            route = Screen.DailyQuoteScreen.route
        ) {
            DailyQuote(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun NavigationItemsBar(
    navController: NavController,
) {
    val currentActivity = LocalContext.current.findActivity()
    val windowSize = calculateWindowSizeClass(activity = currentActivity)
    val currentRoute = navController.currentDestination?.route
    val itemList: List<NavigationItem> = listOf(

        NavigationItem(
            title = stringResource(R.string.todo_list),
            selectedIcon = ImageVector.vectorResource(R.drawable.unselected_todo),
            unselectedIcon = ImageVector.vectorResource(R.drawable.unselected_todo),
            route = Screen.TodoListScreen.route
        ),
        NavigationItem(
            title = stringResource(R.string.notes),
            selectedIcon = ImageVector.vectorResource(R.drawable.unselected_note),
            unselectedIcon = ImageVector.vectorResource(R.drawable.unselected_note),
            route = Screen.NotesScreen.route
        ),

        NavigationItem(
            title = stringResource(R.string.favorites),
            selectedIcon = ImageVector.vectorResource(R.drawable.unselected_fav),
            unselectedIcon = ImageVector.vectorResource(R.drawable.unselected_fav),
            route = Screen.FavNotesScreen.route
        )
    )
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            Box {
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
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BrandGreen,
                                selectedTextColor = BrandGreen,
                                unselectedIconColor = IconColorGray,
                                unselectedTextColor = IconColorGray,
                                indicatorColor = MaterialTheme.colorScheme.background
                            )
                        )
                    }

                }

                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

// I think it's best to limit this NavigationDrawer to the compact screen size only,
// and figure out an alternative for it in medium and expanded sizes
// where other NavigationDrawers are used
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun NavigationDrawer(
    navController: NavController,
    drawerState: DrawerState,
    content: @Composable () -> Unit,
    scope: CoroutineScope
) {
    val currentActivity = LocalContext.current.findActivity()
    val windowSize = calculateWindowSizeClass(activity = currentActivity)
    val currentRoute = navController.currentDestination?.route
    val itemList: List<NavigationItem> = listOf(
        NavigationItem(
            title = stringResource(R.string.home),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = Screen.NotesScreen.route
        ),
        NavigationItem(
            title = stringResource(R.string.deleted),
            selectedIcon = Icons.Filled.Delete,
            unselectedIcon = Icons.Outlined.Delete,
            route = Screen.DeletedNotesScreen.route
        ),
        NavigationItem(
            title = stringResource(R.string.daily_quote),
            selectedIcon = Icons.Default.Email,
            unselectedIcon = Icons.Outlined.Email,
            route = Screen.DailyQuoteScreen.route
        )
    )
    val basicNavigationItems: List<NavigationItem> = listOf(
        NavigationItem(
            title = stringResource(R.string.todo_list),
            selectedIcon = ImageVector.vectorResource(R.drawable.unselected_todo),
            unselectedIcon = ImageVector.vectorResource(R.drawable.unselected_todo),
            route = Screen.TodoListScreen.route
        ),
//        NavigationItem(
//            title = stringResource(R.string.notes),
//            selectedIcon = ImageVector.vectorResource(R.drawable.unselected_note),
//            unselectedIcon = ImageVector.vectorResource(R.drawable.unselected_note),
//            route = Screen.NotesScreen.route
//        ),

        NavigationItem(
            title = stringResource(R.string.favorites),
            selectedIcon = ImageVector.vectorResource(R.drawable.unselected_fav),
            unselectedIcon = ImageVector.vectorResource(R.drawable.unselected_fav),
            route = Screen.FavNotesScreen.route
        )
    )
    if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact)
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.fillMaxHeight()
                ) {
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
    else
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        //.width(170.dp)
                        .endBorder(color = MaterialTheme.colorScheme.secondary)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    itemList.forEach { navigationItem ->
                        NavigationDrawerItem(
                            label = { Text(text = navigationItem.title) },
                            selected = currentRoute == navigationItem.route,
                            onClick = {
                                navController.navigate(navigationItem.route)
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
                                .width(IntrinsicSize.Min)
                                .padding(NavigationDrawerItemDefaults.ItemPadding)

                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    )
                    {
                        basicNavigationItems.forEach { basicNavigationItem ->

                            NavigationRailItem(
                                selected = currentRoute == basicNavigationItem.route,
                                onClick = {
                                    navController.navigate(basicNavigationItem.route)
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (currentRoute == basicNavigationItem.route) {
                                            basicNavigationItem.selectedIcon
                                        } else basicNavigationItem.unselectedIcon,
                                        contentDescription = basicNavigationItem.title
                                    )
                                },
                                label = {
                                    Text(text = basicNavigationItem.title)
                                }
                            )

                        }
                    }

                }
            },
            content = content
        )
}

@Composable
fun NavigationSideBar(
    navController: NavController,
) {
    val currentRoute = navController.currentDestination?.route
    val itemList: List<NavigationItem> = listOf(
        NavigationItem(
            title = stringResource(R.string.todo_list),
            selectedIcon = ImageVector.vectorResource(R.drawable.unselected_todo),
            unselectedIcon = ImageVector.vectorResource(R.drawable.unselected_todo),
            route = Screen.TodoListScreen.route
        ),
        NavigationItem(
            title = stringResource(R.string.notes),
            selectedIcon = ImageVector.vectorResource(R.drawable.unselected_note),
            unselectedIcon = ImageVector.vectorResource(R.drawable.unselected_note),
            route = Screen.NotesScreen.route
        ),

        NavigationItem(
            title = stringResource(R.string.favorites),
            selectedIcon = ImageVector.vectorResource(R.drawable.unselected_fav),
            unselectedIcon = ImageVector.vectorResource(R.drawable.unselected_fav),
            route = Screen.FavNotesScreen.route
        )
    )
    NavigationRail(

    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom)
        ) {
            itemList.forEach { navigationItem ->
                NavigationRailItem(
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
}

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

fun Modifier.endBorder(
    width: Dp = 1.dp,
    color: Color = Color.Black
) = this.drawWithContent {
    drawContent()
    drawLine(
        color,
        start = Offset(size.width - width.toPx(), 0f),
        end = Offset(size.width - width.toPx(), size.height),
        strokeWidth = width.toPx(),
        cap = androidx.compose.ui.graphics.drawscope.Stroke.DefaultCap
    )
}