package com.example.currencyExchangeApplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ConversionHistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversionHistoryDao(): ConversionHistoryDao
}