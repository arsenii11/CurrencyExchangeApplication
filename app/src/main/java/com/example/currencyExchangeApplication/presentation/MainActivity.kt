package com.example.currencyExchangeApplication.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.currencyExchangeApplication.data.database.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.model.CurrencyState
import com.example.currencyExchangeApplication.data.model.ExchangeScreenActions
import com.example.currencyExchangeApplication.data.model.ExchangeScreenState
import com.example.currencyExchangeApplication.data.model.ExchangeState
import com.example.currencyExchangeApplication.presentation.exchangescreen.ExchangeScreen
import com.example.currencyExchangeApplication.presentation.exchangescreen.MainViewModel
import com.example.currencyExchangeApplication.presentation.history.HistoryActivity
import com.example.currencyExchangeApplication.presentation.utilities.MyReceiver
import com.example.currencyExchangeApplication.presentation.utilities.Utility
import com.example.currencyExchangeApplication.presentation.utilities.Links
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val receiver = MyReceiver()
    private val snackbarHostState = SnackbarHostState()

    // Encapsulate state in a single data class
    private var exchangeState by mutableStateOf(
        ExchangeState(
            cur1 = "EUR",
            cur2 = "USD",
            num1 = "",
            num2 = "",
            isLoading = false
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.createDataBase(applicationContext)
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))

        setContent {
            ExchangeScreen(
                state = ExchangeScreenState(
                    currency1 = CurrencyState(
                        selectedCurrency = exchangeState.cur1,
                        amount = exchangeState.num1,
                        onCurrencyChange = {
                            if(it != exchangeState.cur2) {
                                exchangeState = exchangeState.copy(cur1 = it)
                            } else {
                                showSnackbar("Cannot select the same currency for both fields")
                            }
                        },
                        onAmountChange = { exchangeState = exchangeState.copy(num1 = it) }
                    ),
                    currency2 = CurrencyState(
                        selectedCurrency = exchangeState.cur2,
                        amount = exchangeState.num2,
                        onCurrencyChange = {
                            if(it != exchangeState.cur1) {
                                exchangeState = exchangeState.copy(cur2 = it)
                            } else {
                                showSnackbar("Cannot select the same currency for both fields")
                            }
                        },
                        onAmountChange = { exchangeState = exchangeState.copy(num2 = it) }
                    ),
                    isLoading = exchangeState.isLoading,
                    snackbarHostState = snackbarHostState
                ),
                actions = ExchangeScreenActions(
                    onSwapClick = { swapCurrencies() },
                    onDoneClick = { handleDoneClick() },
                    onHistoryClick = { navigateToHistory() }
                )
            )
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        // Предполагается, что ExchangeState реализует Parcelable
        savedInstanceState.putParcelable("exchangeState", exchangeState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        exchangeState = savedInstanceState.getParcelable("exchangeState") ?: ExchangeState()
    }

    private fun navigateToHistory() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleDoneClick() {
        Utility.hideKeyboard(this)
        val numberToConvert = exchangeState.num1

        when {
            numberToConvert.isBlank() || numberToConvert == "0" -> showSnackbar("Empty or invalid input")
            numberToConvert.toDoubleOrNull()?.let { it <= 0 } == true -> showSnackbar("Amount must be greater than 0")
            !Utility.isNetworkAvailable(this) -> showSnackbar("Internet unavailable")
            else -> lifecycleScope.launch { performConversion() }
        }
    }

    private fun showSnackbar(message: String) {
        lifecycleScope.launch { snackbarHostState.showSnackbar(message) }
    }

    @SuppressLint("DefaultLocale")
    private fun performConversion() {
        exchangeState = exchangeState.copy(isLoading = true)
        val apiKey = Links.API_KEY

        viewModel.getExchangeData(
            accessKey = apiKey,
            from = exchangeState.cur1,
            to = exchangeState.cur2,
            amount = exchangeState.num1.toDouble()
        )

        viewModel.myResponse.observe(this@MainActivity) { response ->
            if (response.isSuccessful) {
                response.body()?.rates?.entries?.lastOrNull()?.let { entry ->
                    val convertedValue = entry.value.rate.toDouble() * exchangeState.num1.toDouble()
                    val formattedValue = String.format("%.2f", convertedValue)
                    exchangeState = exchangeState.copy(num2 = formattedValue)

                    // Save conversion details
                    lifecycleScope.launch {
                        viewModel.performConversionAndSave(
                            from = exchangeState.cur1,
                            to = exchangeState.cur2,
                            amount = exchangeState.num1,
                            convertedValue = formattedValue
                        )
                        viewModel.addEntity(
                            ConversionHistoryEntity(
                                id = 0,
                                fromCurrency = exchangeState.cur1,
                                toCurrency = exchangeState.cur2,
                                amount = exchangeState.num1,
                                convertedValue = formattedValue
                            )
                        )
                    }
                }
            } else {
                showSnackbar("Request error")
            }
            exchangeState = exchangeState.copy(isLoading = false)
        }
    }

    private fun swapCurrencies() {
        exchangeState = exchangeState.copy(
            cur1 = exchangeState.cur2,
            cur2 = exchangeState.cur1,
            num1 = exchangeState.num2,
            num2 = exchangeState.num1
        )
    }
}



