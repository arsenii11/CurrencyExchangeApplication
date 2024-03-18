package com.example.currencyExchangeApplication.presentation.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.currencyExchangeApplication.data.database.AppDatabase
import com.example.currencyExchangeApplication.data.database.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.model.ApiResponse
import com.example.currencyExchangeApplication.domain.DataUseCase
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class MainViewModel @Inject constructor (private val repository: DataUseCase): ViewModel(){

    val myResponse: MutableLiveData<Response<ApiResponse>> = MutableLiveData()


    //public
    var convertedRate = MutableLiveData<Double>()

    private lateinit var db: AppDatabase
    fun createDataBase(applicationContext: Context) {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "conversion_history_database"
        ).build()
    }


    fun getExchangeData(accessKey: String, from: String, to: String, amount: Double){
        viewModelScope.launch {
            val response: Response<ApiResponse> = repository.invoke(accessKey, from, to, amount)
            myResponse.value= response
        }
    }


    fun performConversionAndSave(from: String, to: String, amount: String, convertedValue: String) {
        viewModelScope.launch {
            saveConversionTransaction(from, to, amount, convertedValue)
        }
    }
     suspend fun saveConversionTransaction(from: String, to: String, amount: String, convertedValue: String) {
        val conversionEntity = ConversionHistoryEntity(
            fromCurrency = from,
            toCurrency = to,
            amount = amount,
            convertedValue = convertedValue
        )
        db.conversionHistoryDao().insertTransaction(conversionEntity)
    }
}
