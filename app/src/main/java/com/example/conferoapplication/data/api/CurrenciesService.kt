package com.example.conferoapplication.data.api

import com.example.conferoapplication.Utilities.Links
import com.example.conferoapplication.data.model.Currencies
import retrofit2.http.GET
import retrofit2.http.Query


interface CurrenciesService {

    @GET(Links.CUR_LIST_URL)
    suspend fun getSupportedCurrencies(
        @Query("api_key") access_key: String = Links.API_KEY,
        @Query("format") format: String = "JSON",
    ): List<Currencies>
}
