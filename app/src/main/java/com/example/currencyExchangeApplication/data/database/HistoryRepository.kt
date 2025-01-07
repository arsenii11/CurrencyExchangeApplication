package com.example.currencyExchangeApplication.data.database

import androidx.lifecycle.LiveData
import com.example.currencyExchangeApplication.data.database.dao.ConversionHistoryDao
import com.example.currencyExchangeApplication.data.database.entities.ConversionHistoryEntity
import com.example.currencyExchangeApplication.presentation.history.HistoryListItem

class HistoryRepository(private val historyDao: ConversionHistoryDao) {

    val readAllData: LiveData<List<ConversionHistoryEntity>> = historyDao.getLatestTransactions()

    suspend fun deleteAll() {
        historyDao.deleteAllTransactions()
    }
    fun getHistoryWithRates(): LiveData<List<HistoryListItem>> {
        return historyDao.getHistoryWithRates()
    }
}
