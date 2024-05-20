package com.example.currencyExchangeApplication.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class HistoryListItem(
    val fromCurrency: String,
    val toCurrency: String,
    val amount: String,
    val convertedValue: String
)

@Composable
fun HistoryListElement(historyListItem: HistoryListItem) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "From:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = historyListItem.fromCurrency,
                        fontSize = 14.sp
                    )
                    Text(
                        text = historyListItem.amount,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "To:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(Modifier.weight(1f).padding(start = 8.dp)) {
                    Text(
                        text = historyListItem.toCurrency,
                        fontSize = 14.sp
                    )
                    Text(
                        text = historyListItem.convertedValue,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryListElement() {
    val sampleItem = HistoryListItem(
        fromCurrency = "USD",
        toCurrency = "EUR",
        amount = "100",
        convertedValue = "85"
    )
    HistoryListElement(historyListItem = sampleItem)
}
