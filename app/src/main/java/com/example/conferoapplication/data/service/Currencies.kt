package com.example.conferoapplication.data.service

import kotlinx.serialization.Serializable

@Serializable
data class Currencies(
    val currencyTic: String,
    val currencyName: String
)
