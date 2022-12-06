package com.example.conferoapplication.repo

import com.example.conferoapplication.model.Currency
import com.example.conferoapplication.service.CurrenciesService
import javax.inject.Inject

class ExchangeDataSource @Inject constructor(private val exchangeService: CurrenciesService) {

    suspend fun getSupportedCurrencies(): List<Currency> {
        // TODO
        return emptyList()
    }

}