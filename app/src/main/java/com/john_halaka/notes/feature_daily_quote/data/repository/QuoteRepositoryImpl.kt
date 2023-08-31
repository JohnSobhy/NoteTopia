package com.john_halaka.notes.feature_daily_quote.data.repository

import android.util.Log
import com.john_halaka.notes.feature_daily_quote.domain.RetrofitInstance
import com.john_halaka.notes.feature_daily_quote.domain.model.Quote
import com.john_halaka.notes.feature_daily_quote.domain.repository.QuoteRepository

class QuoteRepositoryImpl : QuoteRepository {

    private val quoteService = RetrofitInstance.quoteService
    override suspend fun getQuote(): List<Quote> {
        Log.d("QuoteRepo", "getQuote is called")

        val response = quoteService.getQuotes()
        return response.body() ?: emptyList()
    }
}