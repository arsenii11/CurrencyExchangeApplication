package com.example.currencyExchangeApplication.presentation

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.example.currencyExchangeApplication.data.database.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.model.Rates
import com.example.currencyExchangeApplication.presentation.exchangescreen.ExchangeScreen
import com.example.currencyExchangeApplication.presentation.history.HistoryActivity
import com.example.currencyExchangeApplication.presentation.utilities.MyReceiver
import com.example.currencyExchangeApplication.presentation.utilities.Utility
import com.example.currencyExchangeApplication.presentation.vm.MainViewModel
import com.example.currencyExchangeApplication.utilities.Links
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val receiver = MyReceiver()
    private var cur1 by mutableStateOf("EUR")
    private var cur2 by mutableStateOf("USD")
    private var num1 by mutableStateOf("")
    private var num2 by mutableStateOf("")
    private var isLoading by mutableStateOf(false)
    private val snackbarHostState = SnackbarHostState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.createDataBase(applicationContext)
        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(receiver, intentFilter)

        setContent {
            ExchangeScreen(
                cur1 = cur1,
                cur2 = cur2,
                num1 = num1,
                num2 = num2,
                onCur1Change = { cur1 = it },
                onCur2Change = { cur2 = it },
                onNum1Change = { num1 = it },
                onNum2Change = { num2 = it },
                onSwapClick = { swap() },
                onDoneClick = { onDoneClickListener() },
                onHistoryClick = { onHistoryClick() },
                isLoading = isLoading,
                snackbarHostState = snackbarHostState
            )
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString("cur1", cur1)
        savedInstanceState.putString("cur2", cur2)
        savedInstanceState.putString("num1", num1)
        savedInstanceState.putString("num2", num2)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        cur1 = savedInstanceState.getString("cur1", "EUR")
        cur2 = savedInstanceState.getString("cur2", "USD")
        num1 = savedInstanceState.getString("num1", "")
        num2 = savedInstanceState.getString("num2", "")
    }

    private fun onHistoryClick() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onDoneClickListener() {
        Utility.hideKeyboard(this)
        val numberToConvert = num1
        num2 = "" // Clear the result window

        when {
            numberToConvert.isBlank() || numberToConvert == "0" -> {
                showSnackbar("Empty input")
            }
            !Utility.isNetworkAvailable(this) -> {
                showSnackbar("Internet unavailable")
            }
            else -> {
                lifecycleScope.launch {
                    try {
                        doConversion()
                    } catch (e: NumberFormatException) {
                        showSnackbar("Invalid number format")
                    }
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        lifecycleScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    private suspend fun doConversion() {
        isLoading = true
        val apiKey = Links.API_KEY
        val from = cur1
        val to = cur2
        val amount = num1.toDouble()

        viewModel.getExchangeData(apiKey, from, to, amount)
        viewModel.myResponse.observe(this@MainActivity) { response ->
            if (response.isSuccessful) {
                val ratesMap: Map<String, Rates> = response.body()!!.rates
                if (ratesMap.isNotEmpty()) {
                    val entry = ratesMap.entries.last()
                    val convertedValue = entry.value.rate.toDouble() * amount
                    viewModel.convertedRate.value = convertedValue
                    val finalString = String.format("%.2f", viewModel.convertedRate.value)

                    num2 = finalString

                    lifecycleScope.launch {
                        viewModel.performConversionAndSave(
                            from = from,
                            to = to,
                            amount = amount.toString(),
                            convertedValue = finalString
                        )
                        val newEntity = ConversionHistoryEntity(0, from, to, amount.toString(), finalString)
                        viewModel.addEntity(newEntity)
                    }
                }
                isLoading = false
            } else {
                isLoading = false
                showSnackbar("Request error")
            }
        }
    }

    private fun swap() {
        val tempCur = cur1
        cur1 = cur2
        cur2 = tempCur

        val tempNum = num1
        num1 = num2
        num2 = tempNum
    }
}
