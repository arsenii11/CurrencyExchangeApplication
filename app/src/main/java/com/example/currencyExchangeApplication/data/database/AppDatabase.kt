package com.example.currencyExchangeApplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyExchangeApplication.data.database.dao.ConversionHistoryDao
import com.example.currencyExchangeApplication.data.database.dao.ConversionRateDao
import com.example.currencyExchangeApplication.data.database.entities.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.database.entities.ConversionRateRecordEntity

@Database(
    entities = [ConversionHistoryEntity::class, ConversionRateRecordEntity::class],
    version = 2, // Incremented version to reflect new table addition
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun conversionHistoryDao(): ConversionHistoryDao
    abstract fun conversionRateDao(): ConversionRateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Handle migration during development
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
