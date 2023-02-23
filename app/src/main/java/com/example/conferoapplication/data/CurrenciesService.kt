package com.example.conferoapplication.data

import com.example.conferoapplication.Utilities.Links
import retrofit2.http.GET
import retrofit2.http.Query


interface CurrenciesService {

    @GET(Links.CUR_LIST_URL)
    suspend fun getSupportedCurrencies(
        @Query("api_key") access_key: String = Links.API_KEY,
        @Query("format") format: String = "JSON",
    ): List<Currencies>
}
