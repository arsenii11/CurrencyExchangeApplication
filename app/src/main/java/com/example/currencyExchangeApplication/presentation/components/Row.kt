package com.example.currencyExchangeApplication.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun Row(
  /*  amount: Double,
    currencyList: Array<String>,
    onValueChange: (String) -> Unit,*/
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){}}
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
