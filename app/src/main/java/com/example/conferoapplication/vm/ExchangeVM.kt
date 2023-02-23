package com.example.conferoapplication.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conferoapplication.Utilities.Resource
import com.example.conferoapplication.Utilities.SingleLiveEvent
import com.example.conferoapplication.data.model.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeVM @Inject constructor(private val converDI: ConvertDI) :
    ViewModel() {


    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()


    fun loadAvailableCurrencies() = viewModelScope.launch {

    }

    //cached
    val data = SingleLiveEvent<Resource<ApiResponse>>()


    //public
    val convertedRate = MutableLiveData<Double>()


    //Public function to get the result of conversion
    fun getConvertedData(access_key: String, from: String, to: String, amount: Double) {
        viewModelScope.launch {
            converDI.getConvertedData(access_key, from, to, amount).collect {
                data.value = it
            }
        }
    }

}