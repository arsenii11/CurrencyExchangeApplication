package com.example.currencyExchangeApplication.presentation.history

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.currencyExchangeApplication.data.database.entities.ConversionHistoryEntity
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
                requestList = historyData.value, {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, { viewModel.deleteAll() })
        }
    }
}

@Composable
fun HistoryScreen(
    requestList: List<HistoryListItem>,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .padding(top = 30.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            onClick = onBackClick
        ) {
            Text(
                text = "Back",
                style = MaterialTheme.typography.h6
            )
        }
        OutlinedButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .padding(bottom = 30.dp),
            border = BorderStroke(1.dp, Color.Red),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text(
                text = "Delete All",
                style = MaterialTheme.typography.h6,
                color = Color.Red
            )
        }
        if (requestList.isNotEmpty()) {
            LazyColumn {
                items(requestList) { listItem ->
                    HistoryListElement(
                        HistoryListItem(
                            fromCurrency = listItem.fromCurrency,
                            toCurrency = listItem.toCurrency,
                            amount = listItem.amount,
                            convertedValue = listItem.convertedValue,
                            conversionRate = listItem.conversionRate // Используем курс обмена
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
    }
}


@Preview
@Composable
private fun AboutScreenPreview() {
    HistoryScreen(
        requestList = listOf(
            HistoryListItem(
                fromCurrency = "RUB",
                toCurrency = "EUR",
                amount = "1000.0",
                convertedValue = "10.0",
                conversionRate = "23"
            )
        ), {}, {}
    )
}
