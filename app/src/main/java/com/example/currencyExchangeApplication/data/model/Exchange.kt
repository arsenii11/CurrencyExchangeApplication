package com.example.currencyExchangeApplication.data.model


data class Exchange(
    val amount: String,
    val baseCurrencyCode: String,
    val baseCurrencyName: String,
    val rates:  HashMap<String, Rates> = HashMap(),
    val status: String,
    val updatedDate: String
)