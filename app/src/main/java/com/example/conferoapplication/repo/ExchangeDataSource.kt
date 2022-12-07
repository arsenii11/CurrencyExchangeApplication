package com.example.conferoapplication.repo

import com.example.conferoapplication.model.Currency
import com.example.conferoapplication.service.CurrenciesService
import com.example.conferoapplication.service.ExchangeService
import javax.inject.Inject

class ExchangeDataSource @Inject constructor(private val exchangeService: ExchangeService) {

    suspend fun getConvertedRate(access_key: String, from: String, to: String, amount: Double) =
        exchangeService.convertCurrencies(access_key, from, to, amount)

}