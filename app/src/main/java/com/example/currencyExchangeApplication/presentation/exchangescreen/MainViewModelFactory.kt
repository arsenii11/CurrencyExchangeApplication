package com.example.currencyExchangeApplication.presentation.exchangescreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyExchangeApplication.domain.DataUseCase
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val application: Application,
    private val useCase: DataUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExchangeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExchangeViewModel(application, useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}