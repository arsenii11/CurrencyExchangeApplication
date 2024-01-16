package com.example.currencyExchangeApplication.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun Screen(

) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Toolbar(
                title = "CurrencyExchange",
            )
        },
        content = {
            Box(modifier = Modifier)
        },
    )
}


@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    Screen()
}