package com.example.currencyExchangeApplication.data.model

data class Rates(
    val currencyName: String,
    val rate: String,
    val rateForAmount: Double
)