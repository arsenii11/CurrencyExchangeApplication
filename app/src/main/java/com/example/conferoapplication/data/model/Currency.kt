package com.example.conferoapplication.data.model

data class Currency(val symbol: String, val displayName: String)

data class ApiResponse(
    val amount: String,
    val base_currency_code: String,
    val base_currency_name: String,
    var rates: HashMap<String, Rates> = HashMap(),
    val status: String,
    val updated_date: String
)