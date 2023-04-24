package com.example.currencyExchangeApplication.domain

import com.example.currencyExchangeApplication.data.model.ApiResponse
import retrofit2.Response

interface Repository {
    suspend fun getResponse(): Response<ApiResponse>
}