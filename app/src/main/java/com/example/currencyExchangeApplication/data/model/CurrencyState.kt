package com.example.currencyExchangeApplication.data.model

import androidx.compose.material.SnackbarHostState

// Encapsulates currency-related details
data class CurrencyState(
    val selectedCurrency: String,
    val amount: String,
    val onCurrencyChange: (String) -> Unit,
    val onAmountChange: (String) -> Unit
)

// Encapsulates screen state and actions
data class ExchangeScreenState(
    val currency1: CurrencyState,
    val currency2: CurrencyState,
    val isLoading: Boolean,
    val snackbarHostState: SnackbarHostState
)

// Encapsulates actions performed on the screen
data class ExchangeScreenActions(
    val onSwapClick: () -> Unit,
    val onDoneClick: () -> Unit,
    val onHistoryClick: () -> Unit
)
