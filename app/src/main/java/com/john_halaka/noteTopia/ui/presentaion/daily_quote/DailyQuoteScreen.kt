package com.john_halaka.noteTopia.ui.presentaion.daily_quote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.noteTopia.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyQuote(
    navController: NavController,
    viewModel: DailyQuoteViewModel = hiltViewModel(),
) {
    val dailyQuote by viewModel.quote.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchQuote()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Daily Quote")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        ) {
            val quotes = dailyQuote
            when (quotes) {
                null -> Text(
                    text = "Loading...",
                    modifier = Modifier.padding(16.dp)
                )

                else -> quotes.forEach { quote ->
                    Text(
                        text = "'" + quote.quote + "'",
                        style = Typography.headlineMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = quote.author,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp)

                    )
                }
            }
        }
    }
}