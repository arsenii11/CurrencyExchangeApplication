package com.example.currencyExchangeApplication.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyExchangeApplication.data.model.ApiResponse
import com.example.currencyExchangeApplication.domain.DataUseCase
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class MainViewModel @Inject constructor (private val repository: DataUseCase): ViewModel(){

    val myResponse: MutableLiveData<Response<ApiResponse>> = MutableLiveData()


    //public
    var convertedRate = MutableLiveData<Double>()

    fun getExchangeData(accessKey: String, from: String, to: String, amount: Double){
        viewModelScope.launch {
            val response: Response<ApiResponse> = repository.invoke(accessKey, from, to, amount)
            myResponse.value= response
        }
    }
}
