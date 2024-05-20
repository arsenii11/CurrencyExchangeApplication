package com.example.currencyExchangeApplication.presentation.history

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.currencyExchangeApplication.data.database.ConversionHistoryEntity
import com.example.currencyExchangeApplication.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : ComponentActivity() {

    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
            val historyData = viewModel.readAllData.observeAsState(initial = emptyList())

            HistoryScreen(
                requestList = historyData.value
            ) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}

@Composable
fun HistoryScreen(
    requestList: List<ConversionHistoryEntity>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 100.dp)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (requestList.isNotEmpty()) {
            LazyColumn {
                items(requestList) { listItem ->
                    HistoryListElement(
                        HistoryListItem(
                            fromCurrency = listItem.fromCurrency,
                            toCurrency = listItem.toCurrency,
                            amount = listItem.amount,
                            convertedValue = listItem.convertedValue,
                        )
                    )
                }
            }
        } else {
            Text(
                text = "No History",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 30.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            onClick = {
                onClick()
            }) {
            Text(
                text = "Back",
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    HistoryScreen(
        requestList = listOf(
            ConversionHistoryEntity(
                id = 0,
                fromCurrency = "RUB",
                toCurrency = "EUR",
                amount = "1000.0",
                convertedValue = "10.0",
            )
        )
    ) {}
}
