package com.example.currencyExchangeApplication.presentation.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyExchangeApplication.data.database.AppDatabase
import com.example.currencyExchangeApplication.data.database.entities.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.database.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<HistoryListItem>>
    private val repository: HistoryRepository

    init {
        val historyDao = AppDatabase.getDatabase(application).conversionHistoryDao()
        repository = HistoryRepository(historyDao)
        readAllData = repository.getHistoryWithRates()
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}
