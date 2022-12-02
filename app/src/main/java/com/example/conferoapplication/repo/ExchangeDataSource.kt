package com.example.conferoapplication.repo

import com.example.conferoapplication.model.Currency
import com.example.conferoapplication.service.ExchangeService
import javax.inject.Inject

class ExchangeDataSource @Inject constructor(private val exchangeService: ExchangeService) {

    suspend fun getSupportedCurrencies(): List<Currency> {
        // TODO
        return emptyList()
    }

}