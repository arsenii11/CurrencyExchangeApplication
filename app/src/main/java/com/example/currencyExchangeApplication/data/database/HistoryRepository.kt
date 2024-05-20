package com.example.currencyExchangeApplication.data.database

import androidx.lifecycle.LiveData

class HistoryRepository(private val historyDao: ConversionHistoryDao) {

    val readAllData: LiveData<List<ConversionHistoryEntity>> = historyDao.getLatestTransactions()

    suspend fun addRequest(entity: ConversionHistoryEntity) {
        historyDao.insertTransaction(entity)
    }
}
