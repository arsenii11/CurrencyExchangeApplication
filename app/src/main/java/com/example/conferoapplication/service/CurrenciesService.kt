package com.example.conferoapplication.service

import retrofit2.http.GET


interface CurrenciesService {

    @GET("currency/list")
    suspend fun getSupportedCurrencies(): Map<String, String>

}
