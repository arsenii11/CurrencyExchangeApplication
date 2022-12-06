package com.example.conferoapplication.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conferoapplication.repo.ExchangeDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeVM @Inject constructor(private val exchangeDataSource: ExchangeDataSource): ViewModel() {


    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun loadAvailableCurrencies() = viewModelScope.launch {

    }


}