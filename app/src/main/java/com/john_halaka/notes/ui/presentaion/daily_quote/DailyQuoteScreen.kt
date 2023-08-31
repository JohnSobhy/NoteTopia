package com.john_halaka.notes.ui.presentaion.daily_quote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun DailyQuote(
    navController: NavController,
    viewModel: DailyQuoteViewModel = hiltViewModel(),
) {
    val dailyQuote by viewModel.quote.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchQuote()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val quotes = dailyQuote
        when (quotes) {
            null -> Text(text = "Loading...")
            else -> quotes.forEach { quote ->
                Text(text = quote.quote)
            }
        }
    }
}