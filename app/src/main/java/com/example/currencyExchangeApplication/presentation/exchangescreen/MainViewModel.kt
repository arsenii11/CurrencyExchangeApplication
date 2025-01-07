package com.example.currencyExchangeApplication.presentation.exchangescreen

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.currencyExchangeApplication.data.database.AppDatabase
import com.example.currencyExchangeApplication.data.database.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.database.HistoryRepository
import com.example.currencyExchangeApplication.data.model.ApiResponse
import com.example.currencyExchangeApplication.domain.DataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    application: Application,
    private val repository: DataUseCase
) : AndroidViewModel(application) {

    val myResponse: MutableLiveData<Response<ApiResponse>> = MutableLiveData()

    var convertedRate = MutableLiveData<Double>()

    private lateinit var db: AppDatabase

    fun createDataBase(applicationContext: Context) {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "conversion_history"
        ).build()
    }

    fun getExchangeData(accessKey: String, from: String, to: String, amount: Double) {
        viewModelScope.launch {
            val response: Response<ApiResponse> = repository.invoke(accessKey, from, to, amount)
            myResponse.value = response
        }
    }

    fun performConversionAndSave(from: String, to: String, amount: String, convertedValue: String) {
        viewModelScope.launch {
            saveConversionTransaction(from, to, amount, convertedValue)
        }
    }

    private suspend fun saveConversionTransaction(from: String, to: String, amount: String, convertedValue: String) {
        val conversionEntity = ConversionHistoryEntity(
            fromCurrency = from,
            toCurrency = to,
            amount = amount,
            convertedValue = convertedValue
        )
        db.conversionHistoryDao().insertTransaction(conversionEntity)
    }

    private val readAllData: LiveData<List<ConversionHistoryEntity>>
    private val historyRepository: HistoryRepository

    init {
        val requestHistoryDao = AppDatabase.getDatabase(application).conversionHistoryDao()
        historyRepository = HistoryRepository(requestHistoryDao)
        readAllData = historyRepository.readAllData
    }

    fun addEntity(entity: ConversionHistoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.addRequest(entity)
        }
    }
}