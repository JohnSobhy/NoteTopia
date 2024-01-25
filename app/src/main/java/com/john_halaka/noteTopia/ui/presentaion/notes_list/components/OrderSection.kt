package com.john_halaka.noteTopia.ui.presentaion.notes_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.OrderType

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


//    Column(
//        modifier = modifier,
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//
//            DefaultRadioButton(
//                text = stringResource(R.string.title),
//                selected = noteOrder is NoteOrder.Title,
//                onSelect = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) }
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//
//            DefaultRadioButton(
//                text = stringResource(R.string.date),
//                selected = noteOrder is NoteOrder.Date,
//                onSelect = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) }
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//
//            DefaultRadioButton(
//                text = stringResource(R.string.color),
//                selected = noteOrder is NoteOrder.Color,
//                onSelect = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) }
//            )
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            DefaultRadioButton(
//                text = stringResource(R.string.ascending),
//                selected = noteOrder.orderType is OrderType.Ascending,
//                onSelect = {
//                    onOrderChange(noteOrder.copy(OrderType.Ascending))
//                }
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//
//            DefaultRadioButton(
//                text = stringResource(R.string.descending),
//                selected = noteOrder.orderType is OrderType.Descending,
//                onSelect = {
//                    onOrderChange(noteOrder.copy(OrderType.Descending))
//                }
//            )
//        }
//    }
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


@Preview
@Composable
fun IconRadioButtonPreview() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconRadioButton(
                icon1 = Icons.Filled.Abc,
                icon2 = Icons.Filled.ArrowDownward,
                description = "Sort by Title",
                selected = false,
                onSelect = {},
            )
        }
    }
}