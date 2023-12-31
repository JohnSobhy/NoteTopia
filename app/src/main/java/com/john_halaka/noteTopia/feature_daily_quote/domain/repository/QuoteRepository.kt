package com.john_halaka.noteTopia.feature_daily_quote.domain.repository

import com.john_halaka.noteTopia.feature_daily_quote.domain.model.Quote

interface QuoteRepository {

    suspend fun getQuote(): List<Quote>
}