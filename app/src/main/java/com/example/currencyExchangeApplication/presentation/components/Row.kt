package com.example.currencyExchangeApplication.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun CurrencyRow(
    amount: Double,
    currencyList: Array<String>,
    onValueChange: (String) -> Unit,
) {
    var selectedCurrency by remember { mutableStateOf("EUR") }
    var textValue by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Dropdown
        Box(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colors.background)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            DropdownMenu(
                expanded = false,
                onDismissRequest = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(onClick = { selectedCurrency = "EUR" }) {
                    Text("EUR")
                }
                DropdownMenuItem(onClick = { selectedCurrency = "USD" }) {
                    Text("USD")
                }
                DropdownMenuItem(onClick = { selectedCurrency = "RUB" }) {
                    Text("RUB")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedCurrency,
                    style = MaterialTheme.typography.body1,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(16.dp)
                )
            }
        }

        // OutlinedTextField
        OutlinedTextField(
            value = textValue,
            onValueChange = {
                textValue = it
            },
            label = { Text("Amount") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 8.dp),
        )
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    CurrencyRow(
        12.0, onValueChange = {}, currencyList = arrayOf(
            "EUR",
            "USD",
            "RUB",
            "SEK",
            "ANG",
            "BYN",
            "COP",
            "PLN",
            "UAH",
            "YER",
            "VND",
            "PHP",
            "ISK",
            "KHR",
            "BRL",
            "IDR",
            "SOS"
        )
    )
}
