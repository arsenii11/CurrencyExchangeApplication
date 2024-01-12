package com.example.currencyExchangeApplication.data.repository

import com.example.currencyExchangeApplication.data.api.RetrofitInstance
import com.example.currencyExchangeApplication.data.model.ApiResponse
import com.example.currencyExchangeApplication.domain.CurrencyRepository
import retrofit2.Response
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor() : CurrencyRepository {
    override suspend fun getResponse(
        accessKey: String,
        from: String,
        to: String,
        amount: Double
    ): Response<ApiResponse> = RetrofitInstance.api.convertCurrencies(accessKey, from, to, amount)
}