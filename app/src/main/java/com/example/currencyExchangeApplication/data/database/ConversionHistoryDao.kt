package com.example.currencyExchangeApplication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ConversionHistoryDao {

    @Query("SELECT * FROM conversion_history ORDER BY id DESC LIMIT 5")
    suspend fun getLatestTransactions(): List<ConversionHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: ConversionHistoryEntity)
}
