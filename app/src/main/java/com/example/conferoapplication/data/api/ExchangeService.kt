package com.example.conferoapplication.data.api

import com.example.conferoapplication.Utilities.Links
import com.example.conferoapplication.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeService {
    @GET(Links.CONVERT_URL)
    suspend fun convertCurrencies(
        @Query("api_key") access_key: String = Links.API_KEY,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ) : Response<ApiResponse>

}