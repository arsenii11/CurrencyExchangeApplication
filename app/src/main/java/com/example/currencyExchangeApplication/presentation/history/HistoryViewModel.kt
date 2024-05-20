package com.example.currencyExchangeApplication.presentation.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.currencyExchangeApplication.data.database.AppDatabase
import com.example.currencyExchangeApplication.data.database.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.database.HistoryRepository

class HistoryViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<ConversionHistoryEntity>>
    private val repository: HistoryRepository

    init {
        val historyDao = AppDatabase.getDatabase(application).conversionHistoryDao()
        repository = HistoryRepository(historyDao)
        readAllData = repository.readAllData
    }
}