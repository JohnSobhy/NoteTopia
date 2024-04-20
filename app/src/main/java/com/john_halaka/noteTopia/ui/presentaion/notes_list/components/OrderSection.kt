package com.john_halaka.noteTopia.ui.presentaion.notes_list.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.OrderType
import com.john_halaka.noteTopia.ui.theme.BrandGreen

@Composable
fun SortDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    noteOrder: NoteOrder,
    onOrderChange: (NoteOrder) -> Unit
) {
    val menuItems: List<SortDropDownMenuItem> = listOf(
        SortDropDownMenuItem(
            text = "Date",
            trailingIcon = Icons.Filled.ArrowDownward,
            noteOrder = NoteOrder.Date(OrderType.Descending),
        ),
        SortDropDownMenuItem(
            text = "Date",
            trailingIcon = Icons.Filled.ArrowUpward,
            noteOrder = NoteOrder.Date(OrderType.Ascending),
        ),
        SortDropDownMenuItem(
            text = "Title",
            trailingIcon = Icons.Filled.ArrowDownward,
            noteOrder = NoteOrder.Title(OrderType.Descending),
        ),
        SortDropDownMenuItem(
            text = "Title",
            trailingIcon = Icons.Filled.ArrowUpward,
            noteOrder = NoteOrder.Title(OrderType.Ascending),
        ),
        SortDropDownMenuItem(
            text = "Color",
            trailingIcon = Icons.Filled.ArrowDownward,
            noteOrder = NoteOrder.Color(OrderType.Descending),
        ),
        SortDropDownMenuItem(
            text = "Color",
            trailingIcon = Icons.Filled.ArrowUpward,
            noteOrder = NoteOrder.Color(OrderType.Ascending),
        ),
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        menuItems.chunked(2).forEach { rowItems ->
            Row(Modifier.fillMaxWidth()) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        DropdownMenuItem(
                            leadingIcon = {
                                if (
                                    noteOrder.toString() == (item.noteOrder.toString()) &&
                                    noteOrder.orderType == item.noteOrder.orderType
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        tint = BrandGreen,
                                        contentDescription = ""
                                    )
                                }
                            },
                            text = { Text(text = item.text) },
                            trailingIcon = {
                                Icon(imageVector = item.trailingIcon, contentDescription = "")
                            },
                            onClick = {
                                Log.d("orderSection", "noteOrder is $noteOrder")
                                Log.d("orderSection", "item noteOrder is ${item.noteOrder}")
                                onOrderChange(item.noteOrder)
                                Log.d("orderSection", "noteOrder is $noteOrder")
                                onDismissRequest()
                            }
                        )
                    }
                }
            }

        }
    }
}


data class SortDropDownMenuItem(
    val text: String,
    val trailingIcon: ImageVector,
    val noteOrder: NoteOrder,
)
