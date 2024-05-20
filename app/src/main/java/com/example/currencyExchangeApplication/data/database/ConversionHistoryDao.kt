package com.example.currencyExchangeApplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ConversionHistoryDao {

    @Query("SELECT * FROM conversion_history")
    fun getLatestTransactions(): LiveData<List<ConversionHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: ConversionHistoryEntity)

    @Query("DELETE FROM conversion_history")
    suspend fun deleteAllTransactions()
}
