package com.example.currencyExchangeApplication.data.model



data class ApiResponse(
    val amount: String,
    val baseCurrencyCode: String,
    val baseCurrencyName: String,
    var rates: Map<String, Rates>,
    val status: String,
    val updatedDate: String
)