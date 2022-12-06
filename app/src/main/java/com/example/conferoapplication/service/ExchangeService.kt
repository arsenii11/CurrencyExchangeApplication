package com.example.conferoapplication.service

import com.example.conferoapplication.help.Links
import com.example.conferoapplication.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ExchangeService {

    @GET("currency/list")
    suspend fun getSupportedCurrencies(): Map<String, String>

}

interface ApiService {

    @GET(Links.CONVERT_URL)
    suspend fun convertCurrency(
        @Query("api_key") access_key: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ) : Response<ApiResponse>

}