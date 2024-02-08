package com.john_halaka.noteTopia.ui.presentaion.notes_list.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_note.domain.util.ViewType



data class NotesViewDropDownItem(
    val text: String,
    val leadingIcon: ImageVector,
    val viewType: ViewType
)
@Composable
fun NotesViewDropDownMenu(
    viewMenuExpanded: Boolean,
    onDismiss: ()->Unit,
    viewType: ViewType,
    onViewChanged : (ViewType)-> Unit,
){
    val menuItems: List<NotesViewDropDownItem> = listOf(
        NotesViewDropDownItem(
            text = stringResource(R.string.grid),
            leadingIcon = Icons.Default.GridView,
            viewType = ViewType.GRID
        ),
        NotesViewDropDownItem(
            text = stringResource(R.string.list),
            leadingIcon = Icons.Default.List,
            viewType = ViewType.LIST
        ),
        NotesViewDropDownItem(
            text = stringResource(R.string.small_grid),
            leadingIcon = Icons.Default.GridOn,
            viewType = ViewType.SMALL_GRID
        )
    )
    DropdownMenu(
        expanded = viewMenuExpanded ,
        onDismissRequest = onDismiss
    ) {
        menuItems.forEach{item->
            DropdownMenuItem(
                text = {
                       Text(text = item.text)
                },
                leadingIcon = {
                                 Icon(imageVector = item.leadingIcon, contentDescription = item.text)
                              },
                trailingIcon = {
                               if (item.viewType.name == viewType.name){
                                   Icon(imageVector = Icons.Default.Check, contentDescription = "")
                               }
                },
                onClick = {
                    onViewChanged(item.viewType)
                }
            )

        }
    }
}
