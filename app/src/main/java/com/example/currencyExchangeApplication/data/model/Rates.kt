package com.example.currencyExchangeApplication.data.model

data class Rates(
    val currency_name: String,
    val rate: String,
    val rate_for_amount: Double
)