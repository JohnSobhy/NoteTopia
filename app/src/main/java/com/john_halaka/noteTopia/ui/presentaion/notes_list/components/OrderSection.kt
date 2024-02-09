package com.john_halaka.noteTopia.ui.presentaion.notes_list.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.OrderType
import com.john_halaka.noteTopia.ui.theme.BrandGreen

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()

        ) {
            IconRadioButton(
                icon1 = Icons.Filled.DateRange,
                icon2 = Icons.Filled.ArrowDownward,
                description = "Sort by Date Descending",
                selected = noteOrder is NoteOrder.Date && noteOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(NoteOrder.Date(noteOrder.orderType))
                    onOrderChange(noteOrder.copy(OrderType.Descending))
                },
            )
            Spacer(modifier = Modifier.width(16.dp))

            IconRadioButton(
                icon1 = Icons.Filled.Abc,
                icon2 = Icons.Filled.ArrowDownward,
                description = "Sort by Title Descending",
                selected = noteOrder is NoteOrder.Title && noteOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(NoteOrder.Title(noteOrder.orderType))
                    onOrderChange(noteOrder.copy(OrderType.Descending))
                },
            )
            Spacer(modifier = Modifier.width(16.dp))

            IconRadioButton(
                icon1 = Icons.Default.ColorLens,
                icon2 = Icons.Filled.ArrowDownward,
                description = "Sort by Color Descending",
                selected = noteOrder is NoteOrder.Color && noteOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(NoteOrder.Color(noteOrder.orderType))
                    onOrderChange(noteOrder.copy(OrderType.Descending))
                },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconRadioButton(
                icon1 = Icons.Filled.DateRange,
                icon2 = Icons.Filled.ArrowUpward,
                description = "Sort by Date Ascending",
                selected = noteOrder is NoteOrder.Date && noteOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(NoteOrder.Date(noteOrder.orderType))
                    onOrderChange(noteOrder.copy(OrderType.Ascending))
                },
            )
            Spacer(modifier = Modifier.width(16.dp))

            IconRadioButton(
                icon1 = Icons.Filled.Abc,
                icon2 = Icons.Filled.ArrowUpward,
                description = "Sort by Title Ascending",
                selected = noteOrder is NoteOrder.Title && noteOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(NoteOrder.Title(noteOrder.orderType))
                    onOrderChange(noteOrder.copy(OrderType.Ascending))
                },
            )
            Spacer(modifier = Modifier.width(16.dp))

            IconRadioButton(
                icon1 = Icons.Default.ColorLens,
                icon2 = Icons.Filled.ArrowUpward,
                description = "Sort by Color Ascending",
                selected = noteOrder is NoteOrder.Color && noteOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(NoteOrder.Color(noteOrder.orderType))
                    onOrderChange(noteOrder.copy(OrderType.Ascending))
                },
            )
        }


    }

}

@Composable
fun IconRadioButton(
    icon1: ImageVector,
    icon2: ImageVector,
    description: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Icon(imageVector = icon1, contentDescription = description)
        Icon(imageVector = icon2, contentDescription = description)
    }
}


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
//            menuItems.forEach {item ->
//                DropdownMenuItem(
//                    leadingIcon = {
//                        if (noteOrder.toString() == (item.noteOrder.toString()) && noteOrder.orderType == item.noteOrder.orderType) {
//                            Icon(imageVector = Icons.Default.Check, contentDescription = "")
//                        }
//                    },
//                    text = { Text(text = item.text) },
//                    trailingIcon = {
//                      Icon(imageVector = item.trailingIcon, contentDescription = "")
//                    },
//                    onClick = {
//                        Log.d("orderSection", "noteOrder is $noteOrder")
//                        Log.d("orderSection", "item noteOrder is ${item.noteOrder}")
//                        onOrderChange(item.noteOrder)
//                        Log.d("orderSection", "noteOrder is $noteOrder")
//                    }
//                )
//            }


//    DropdownMenu(
//        expanded = expanded,
//        onDismissRequest = onDismissRequest,
//
//        ) {
//        Row(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                DropdownMenuItem(
//                    text = {
//                        IconRadioButton(
//                            icon1 = Icons.Filled.DateRange,
//                            icon2 = Icons.Filled.ArrowDownward,
//                            description = "Sort by Date Descending",
//                            selected = noteOrder is NoteOrder.Date && noteOrder.orderType is OrderType.Descending,
//                            onSelect = {
//                                onOrderChange(NoteOrder.Date(noteOrder.orderType))
//                                onOrderChange(noteOrder.copy(OrderType.Descending))
//                            },
//                        )
//                    },
//                    onClick = {}
//                )
//                DropdownMenuItem(
//                    text = {
//                        IconRadioButton(
//                            icon1 = Icons.Filled.Abc,
//                            icon2 = Icons.Filled.ArrowDownward,
//                            description = "Sort by Title Descending",
//                            selected = noteOrder is NoteOrder.Title && noteOrder.orderType is OrderType.Descending,
//                            onSelect = {
//                                onOrderChange(NoteOrder.Title(noteOrder.orderType))
//                                onOrderChange(noteOrder.copy(OrderType.Descending))
//                            },
//                        )
//                    },
//                    onClick = { }
//                )
//                DropdownMenuItem(
//                    text = {
//                        IconRadioButton(
//                            icon1 = Icons.Default.ColorLens,
//                            icon2 = Icons.Filled.ArrowDownward,
//                            description = "Sort by Color Descending",
//                            selected = noteOrder is NoteOrder.Color && noteOrder.orderType is OrderType.Descending,
//                            onSelect = {
//                                onOrderChange(NoteOrder.Color(noteOrder.orderType))
//                                onOrderChange(noteOrder.copy(OrderType.Descending))
//                            },
//                        )
//                    },
//                    onClick = { }
//                )
//            }
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                DropdownMenuItem(
//                    text = {
//                        IconRadioButton(
//                            icon1 = Icons.Filled.DateRange,
//                            icon2 = Icons.Filled.ArrowUpward,
//                            description = "Sort by Date Ascending",
//                            selected = noteOrder is NoteOrder.Date && noteOrder.orderType is OrderType.Ascending,
//                            onSelect = {
//                                onOrderChange(NoteOrder.Date(noteOrder.orderType))
//                                onOrderChange(noteOrder.copy(OrderType.Ascending))
//                            },
//                        )
//                    },
//                    onClick = {}
//                )
//                DropdownMenuItem(
//                    text = {
//                        IconRadioButton(
//                            icon1 = Icons.Filled.Abc,
//                            icon2 = Icons.Filled.ArrowUpward,
//                            description = "Sort by Title Ascending",
//                            selected = noteOrder is NoteOrder.Title && noteOrder.orderType is OrderType.Ascending,
//                            onSelect = {
//                                onOrderChange(NoteOrder.Title(noteOrder.orderType))
//                                onOrderChange(noteOrder.copy(OrderType.Ascending))
//                            },
//                        )
//                    },
//                    onClick = {}
//                )
//                DropdownMenuItem(
//                    text = {
//                        IconRadioButton(
//                            icon1 = Icons.Default.ColorLens,
//                            icon2 = Icons.Filled.ArrowUpward,
//                            description = "Sort by Color Ascending",
//                            selected = noteOrder is NoteOrder.Color && noteOrder.orderType is OrderType.Ascending,
//                            onSelect = {
//                                onOrderChange(NoteOrder.Color(noteOrder.orderType))
//                                onOrderChange(noteOrder.copy(OrderType.Ascending))
//                            },
//                        )
//                    },
//                    onClick = {}
//                )
//
//            }
//
//        }
//    }

data class SortDropDownMenuItem(
    val text: String,
    val trailingIcon: ImageVector,
    val noteOrder: NoteOrder,
    //val onOrderChange: (NoteOrder) -> Unit
    //val orderType: OrderType
)
