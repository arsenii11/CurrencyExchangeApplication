package com.example.currencyExchangeApplication.data.model



data class ApiResponse(
    val amount: String,
    val baseCurrencyCode: String,
    val baseCurrencyName: String,
    var rates: HashMap<String, Rates> = HashMap(),
    val status: String,
    val updatedDate: String
)