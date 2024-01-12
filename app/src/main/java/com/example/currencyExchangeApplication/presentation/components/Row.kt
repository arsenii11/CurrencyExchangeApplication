/*
package com.example.currencyExchangeApplication.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.typography
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun Row(
    amount: Double,
    currencyList: Array<String>,
    onValueChange: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){} */
/*{
        // Representing the LinearLayout
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(
                text = "EUR",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f),
                style = typography.caption
            )

            var expanded by remember { mutableStateOf(false) }
            var selectedIndex by remember { mutableStateOf(0) }

            *//*
*/
/*OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = onValueChange
                },
                label = {
                    Text(text = "Amount")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    capitalization = KeyboardCapitalization.None
                ),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp)
                    .weight(1f)
            )*//*
*/
/*


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(60.dp)
            ) {
                listOf("Euro", "USD", "GBP").forEachIndexed { index, item ->
                    DropdownMenuItem(
                        onClick = {
                            selectedIndex = index
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "DropDown",
                    modifier = Modifier
                        .clickable { expanded = true }
                        .rotate(if (expanded) 180f else 0f)
                        .padding(4.dp)
                )
            }
        }
    }*//*

}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    Row(
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
}*/
