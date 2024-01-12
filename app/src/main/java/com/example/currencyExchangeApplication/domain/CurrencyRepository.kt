package com.example.currencyExchangeApplication.domain

import com.example.currencyExchangeApplication.data.model.ApiResponse
import retrofit2.Response

interface CurrencyRepository {
    suspend fun getResponse(
        accessKey: String,
        from: String,
        to: String,
        amount: Double
    ): Response<ApiResponse>
}