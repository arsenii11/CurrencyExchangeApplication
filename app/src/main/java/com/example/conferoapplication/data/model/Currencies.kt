package com.example.conferoapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Currencies(
    val currencyTic: String,
    val currencyName: String
)
