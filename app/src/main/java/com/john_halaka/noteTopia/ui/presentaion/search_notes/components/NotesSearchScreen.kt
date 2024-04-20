package com.john_halaka.noteTopia.ui.presentaion.search_notes.components

//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun NotesSearchScreen(
//    navController: NavController,
//    viewModel: NotesViewModel = hiltViewModel(),
//    context: Context
//
//) {
//    val state = viewModel.state.value
//    val snackbarHostState = remember { SnackbarHostState() }
//    val scope = rememberCoroutineScope()
//    var notesList by remember { mutableStateOf(state.notes) }
//    var searchText by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = MaterialTheme.colorScheme.background)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            TextField(
//                value = searchText,
//                onValueChange = { searchPhrase ->
//                    viewModel.onEvent(NotesEvent.SearchNotes(searchPhrase))
//                    notesList = state.searchResult
//                    searchText = searchPhrase
//                },
//                modifier = Modifier.fillMaxWidth(),
//                colors = TextFieldDefaults.colors(
//                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
//                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
//                ),
//                textStyle = Typography.titleLarge,
//                singleLine = true,
//                placeholder = {
//                    Text(
//                        stringResource(R.string.find_in_your_notes),
//                        style = Typography.titleLarge,
//                        color = MaterialTheme.colorScheme.onTertiary
//                    )
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = stringResource(R.string.back),
//                        modifier = Modifier
//                            .clickable(
//                                true,
//                                onClick = {
//                                    navController.navigateUp()
//                                }
//                            )
//
//                    )
//                },
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = stringResource(R.string.search_icon),
//                        modifier = Modifier
//                            .align(Alignment.CenterVertically)
//                            .padding(start = 16.dp)
//                            .clickable(
//                                true,
//                                onClick = {
//                                    viewModel.onEvent(NotesEvent.SearchNotes(searchText))
//                                    notesList = state.searchResult
//                                }
//                            )
//                    )
//                }
//            )
//        }
//
//        ListViewNotes(
//
//            navController = navController,
//            viewModel = viewModel,
////            scope = scope,
////            snackbarHostState = snackbarHostState,
//            notesList = notesList,
//            context = context,
//            showFavoriteIcon = true
//        )
//
//
//    }
//}
