package com.john_halaka.notes.feature_daily_quote.domain

import com.john_halaka.notes.feature_daily_quote.domain.model.Quote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteService {
    @GET("/v1/quotes")
    suspend fun getQuotes(
        @Query("category") category: String? = null,
        @Query("limit") limit: Int? = null
    ): Response<List<Quote>>
}