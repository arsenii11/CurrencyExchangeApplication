package com.example.currencyExchangeApplication.data.repo

import com.example.currencyExchangeApplication.data.api.ExchangeService
import javax.inject.Inject

class ExchangeDataSource @Inject constructor(private val exchangeService: ExchangeService) {

    suspend fun getConvertedRate(access_key: String, from: String, to: String, amount: Double) =
        exchangeService.convertCurrencies(access_key, from, to, amount)

}