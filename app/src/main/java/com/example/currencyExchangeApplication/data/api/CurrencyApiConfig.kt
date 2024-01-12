package com.example.currencyExchangeApplication.data.api

import com.example.currencyExchangeApplication.Utilities.JsonSerialization
import com.example.currencyExchangeApplication.Utilities.Links
import com.example.currencyExchangeApplication.data.model.ApiResponse
import com.example.currencyExchangeApplication.data.model.Currencies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface CurrencyApiConfig {


    @GET(Links.CUR_LIST_URL)
    suspend fun getSupportedCurrencies(
        @Header("api_key") accessKey: String = Links.API_KEY,
        @Header("format") format: String = "JSON",
    ): List<Currencies>


    @GET(Links.CONVERT_URL)
    suspend fun convertCurrencies(
        @Header("api_key") accessKey: String = Links.API_KEY,
        @Header("from") from: String,
        @Header("to") to: String,
        @Header("amount") amount: Double
    ) : Response<ApiResponse>
}
