package com.john_halaka.notes.ui

sealed class Screen (val route : String) {
    object NotesScreen : Screen("notes_screen")

    object FavNotesScreen : Screen("favourite_notes_screen")
    object DeletedNotesScreen : Screen("deleted_notes_screen")

    object AddEditNoteScreen : Screen("add_edit_note_screen")

    object NotesSearchScreen : Screen("notes_search_screen")

    object TodoListScreen : Screen("todo_list")

    object DailyQuoteScreen : Screen("daily_quote")
    object AddEditTodoScreen : Screen("add_edit_todo")
}