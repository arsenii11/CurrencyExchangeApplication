package com.example.currencyExchangeApplication.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.currencyExchangeApplication.presentation.utilities.composeStyles.AppTextStyle

@Composable
fun Toolbar(
    title: String,
) {
    Row(
        modifier = Modifier.padding(12.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title.uppercase(),
            style = AppTextStyle.h1
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun toolbarPreview() {
    Toolbar(title = "Hello everyone")
}