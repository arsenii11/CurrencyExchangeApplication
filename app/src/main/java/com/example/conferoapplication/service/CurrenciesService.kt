package com.example.conferoapplication.service

import com.example.conferoapplication.Utilities.Links
import retrofit2.http.GET
import retrofit2.http.Query


interface CurrenciesService {

    @GET("currency/list")
    suspend fun getSupportedCurrencies(
        @Query("api_key") access_key: String = Links.API_KEY,
    ): Map<String, String>

}
