package com.example.currencyExchangeApplication.presentation.exchangescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conferoapplication.R
import com.example.currencyExchangeApplication.presentation.utilities.CurrenciesAvailable

@Composable
fun ExchangeScreen(
    cur1: String,
    cur2: String,
    num1: String,
    num2: String,
    onCur1Change: (String) -> Unit,
    onCur2Change: (String) -> Unit,
    onNum1Change: (String) -> Unit,
    onNum2Change: (String) -> Unit,
    onSwapClick: () -> Unit,
    onDoneClick: () -> Unit,
    onHistoryClick: () -> Unit,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Currency Exchange",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(top = 40.dp)
        )

        CurrencyRow(
            selectedCurrency = cur1,
            currencies = CurrenciesAvailable.currenciesList(),
            amount = num1,
            onCurrencyChange = onCur1Change,
            onAmountChange = onNum1Change
        )

        IconButton(
            onClick = onSwapClick,
            modifier = Modifier
                .size(48.dp)
                .background(Color.LightGray)
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_swap),
                contentDescription = null,
                tint = Color.Black
            )
        }

        CurrencyRow(
            selectedCurrency = cur2,
            currencies = CurrenciesAvailable.currenciesList(),
            amount = num2,
            onCurrencyChange = onCur2Change,
            onAmountChange = onNum2Change
        )

        Button(
            onClick = onDoneClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(text = "Done", color = Color.White, fontSize = 18.sp)
        }

        Button(
            onClick = onHistoryClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
        ) {
            Text(text = "History", color = Color.White, fontSize = 18.sp)
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                backgroundColor = Color.Red,
                contentColor = Color.White,
                snackbarData = data
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewExchangeScreen() {
    ExchangeScreen(
        cur1 = "EUR",
        cur2 = "USD",
        num1 = "100",
        num2 = "120",
        onCur1Change = {},
        onCur2Change = {},
        onNum1Change = {},
        onNum2Change = {},
        onSwapClick = {},
        onDoneClick = {},
        onHistoryClick = {},
        isLoading = false,
        snackbarHostState = SnackbarHostState()
    )
}
