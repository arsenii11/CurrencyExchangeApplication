package com.example.conferoapplication.service

data class Exchange(
    val amount: String,
    val base_currency_code: String,
    val base_currency_name: String,
    val rates:  HashMap<String, Rates> = HashMap(),
    val status: String,
    val updated_date: String
)