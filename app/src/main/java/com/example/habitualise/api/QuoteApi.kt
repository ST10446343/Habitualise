package com.example.habitualise.api

import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteApi {

    @GET("quotes/random")
    suspend fun getQuote(
        @Query("limit")
        limit: Int = 1
    ): List<Quote>
}