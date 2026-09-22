package com.example.habitualise.api

class QuoteRepository {

    suspend fun getRandomQuote(): Result<Quote> {
        return try {
            val quotes = RetrofitInstance.quoteApi.getQuote(1)

            if (quotes.isEmpty()) {
                Result.failure(
                    Exception("No quote was returned.")
                )
            } else {
                Result.success(quotes.first())
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}