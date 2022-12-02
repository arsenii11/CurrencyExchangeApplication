package com.example.conferoapplication.service

import retrofit2.http.GET


interface ExchangeService {

    @GET("currency/list")
    suspend fun getSupportedCurrencies(): Map<String, String>

}