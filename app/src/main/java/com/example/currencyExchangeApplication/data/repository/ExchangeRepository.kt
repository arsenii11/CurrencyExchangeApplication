// File: com/example/currencyExchangeApplication/data/repository/ExchangeRepository.kt

package com.example.currencyExchangeApplication.data.repository

import androidx.lifecycle.LiveData
import com.example.currencyExchangeApplication.data.api.RetrofitInstance
import com.example.currencyExchangeApplication.data.database.dao.ConversionHistoryDao
import com.example.currencyExchangeApplication.data.database.dao.ConversionRateDao
import com.example.currencyExchangeApplication.data.database.dao.UserPreferencesDao
import com.example.currencyExchangeApplication.data.database.entities.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.database.entities.ConversionRateRecordEntity
import com.example.currencyExchangeApplication.data.database.entities.UserPreferencesEntity
import com.example.currencyExchangeApplication.data.model.ApiResponse
import com.example.currencyExchangeApplication.presentation.history.HistoryListItem
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRepository @Inject constructor(
    private val conversionHistoryDao: ConversionHistoryDao,
    private val conversionRateDao: ConversionRateDao,
    private val userPreferencesDao: UserPreferencesDao
) {

    suspend fun convertCurrencies(
        accessKey: String,
        from: String,
        to: String,
        amount: Double
    ): Response<ApiResponse> = RetrofitInstance.api.convertCurrencies(accessKey, from, to, amount)

    /**
     * Inserts a ConversionHistoryEntity and returns the generated ID.
     */
    suspend fun insertConversionHistory(conversionHistoryEntity: ConversionHistoryEntity): Long {
        return conversionHistoryDao.insertTransaction(conversionHistoryEntity)
    }

    /**
     * Inserts a ConversionRateRecordEntity.
     */
    suspend fun insertConversionRateRecord(rateRecordEntity: ConversionRateRecordEntity) {
        conversionRateDao.insertRateRecord(rateRecordEntity)
    }

    /**
     * Retrieves all conversion history records.
     *
     * @return LiveData emitting a list of [ConversionHistoryEntity].
     */
    fun getLatestTransactions(): LiveData<List<ConversionHistoryEntity>> {
        return conversionHistoryDao.getLatestTransactions()
    }

    /**
     * Retrieves conversion history along with their associated rate records.
     *
     * @return LiveData emitting a list of [HistoryListItem].
     */
    fun getHistoryWithRates(): LiveData<List<HistoryListItem>> {
        return conversionHistoryDao.getHistoryWithRates()
    }

    /**
     * Deletes all conversion history records from the database.
     */
    suspend fun deleteAllConversions() {
        conversionHistoryDao.deleteAllTransactions()
    }

    // ------------------------------
    // User Preferences Operations
    // ------------------------------

    /**
     * Saves user preferences to the database.
     *
     * @param userPreferencesEntity The user preferences entity to save.
     */

    /**
     * Retrieves user preferences based on the user ID.
     *
     * @param userId The ID of the user.
     * @return The [UserPreferencesEntity] if found, else null.
     */
    suspend fun getUserPreferences(userId: Long): UserPreferencesEntity? {
        return userPreferencesDao.getUserPreferences(userId)
    }

    /**
     * Deletes user preferences based on the user ID.
     *
     * @param userId The ID of the user.
     */

}
