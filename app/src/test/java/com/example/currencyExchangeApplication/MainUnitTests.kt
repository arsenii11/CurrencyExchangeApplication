package com.example.currencyExchangeApplication

import androidx.lifecycle.MutableLiveData
import com.example.currencyExchangeApplication.data.api.CurrencyApiConfig
import com.example.currencyExchangeApplication.data.api.RetrofitInstance
import com.example.currencyExchangeApplication.data.database.dao.ConversionHistoryDao
import com.example.currencyExchangeApplication.data.database.dao.ConversionRateDao
import com.example.currencyExchangeApplication.data.database.dao.QuickAccessPairsDao
import com.example.currencyExchangeApplication.data.database.entities.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.database.entities.ConversionRateRecordEntity
import com.example.currencyExchangeApplication.data.database.entities.QuickAccessPairsEntity
import com.example.currencyExchangeApplication.data.model.ApiResponse
import com.example.currencyExchangeApplication.data.repository.ExchangeRepository
import com.example.currencyExchangeApplication.presentation.history.HistoryListItem
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import org.junit.Assert.assertEquals

class ExchangeRepositoryTest {

    // Mocks
    private val conversionHistoryDao = mockk<ConversionHistoryDao>()
    private val conversionRateDao = mockk<ConversionRateDao>()
    private val quickAccessPairsDao = mockk<QuickAccessPairsDao>()
    private val mockApi = mockk<CurrencyApiConfig>()

    // System Under Test
    private lateinit var repository: ExchangeRepository

    @Before
    fun setup() {
        // Mock RetrofitInstance.api
        mockkObject(RetrofitInstance)
        every { RetrofitInstance.api } returns mockApi

        repository = ExchangeRepository(
            conversionHistoryDao = conversionHistoryDao,
            conversionRateDao = conversionRateDao,
            quickAccessPairsDao = quickAccessPairsDao
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `convertCurrencies should return API response`() = runTest {
        // Given
        val accessKey = "test_key"
        val from = "USD"
        val to = "EUR"
        val amount = 100.0
        val expectedResponse = mockk<Response<ApiResponse>>()

        coEvery {
            mockApi.convertCurrencies(accessKey, from, to, amount)
        } returns expectedResponse

        // When
        val result = repository.convertCurrencies(accessKey, from, to, amount)

        // Then
        assertEquals(expectedResponse, result)
        coVerify { mockApi.convertCurrencies(accessKey, from, to, amount) }
    }

    @Test
    fun `insertConversionHistory should return generated ID`() = runTest {
        // Given
        val entity = ConversionHistoryEntity(
            fromCurrency = "USD",
            toCurrency = "EUR",
            amount = "100.0",
            convertedValue = "92.5"
        )
        val expectedId = 123L

        coEvery { conversionHistoryDao.insertTransaction(entity) } returns expectedId

        // When
        val result = repository.insertConversionHistory(entity)

        // Then
        assertEquals(expectedId, result)
        coVerify { conversionHistoryDao.insertTransaction(entity) }
    }

    @Test
    fun `insertConversionRateRecord should call DAO`() = runTest {
        // Given
        val rateRecord = ConversionRateRecordEntity(
            conversionId = 123L,
            rateAtConversion = "0.925",
            timestamp = System.currentTimeMillis()
        )

        coEvery { conversionRateDao.insertRateRecord(rateRecord) } just Runs

        // When
        repository.insertConversionRateRecord(rateRecord)

        // Then
        coVerify { conversionRateDao.insertRateRecord(rateRecord) }
    }

    @Test
    fun `getLatestTransactions should return LiveData from DAO`() {
        // Given
        val expectedLiveData = MutableLiveData<List<ConversionHistoryEntity>>()
        every { conversionHistoryDao.getLatestTransactions() } returns expectedLiveData

        // When
        val result = repository.getLatestTransactions()

        // Then
        assertEquals(expectedLiveData, result)
        verify { conversionHistoryDao.getLatestTransactions() }
    }

    @Test
    fun `getHistoryWithRates should return LiveData from DAO`() {
        // Given
        val expectedLiveData = MutableLiveData<List<HistoryListItem>>()
        every { conversionHistoryDao.getHistoryWithRates() } returns expectedLiveData

        // When
        val result = repository.getHistoryWithRates()

        // Then
        assertEquals(expectedLiveData, result)
        verify { conversionHistoryDao.getHistoryWithRates() }
    }

    @Test
    fun `deleteAllConversions should call DAO`() = runTest {
        // Given
        coEvery { conversionHistoryDao.deleteAllTransactions() } just Runs

        // When
        repository.deleteAllConversions()

        // Then
        coVerify { conversionHistoryDao.deleteAllTransactions() }
    }

    @Test
    fun `getQuickAccessPairs should return pairs from DAO`() = runTest {
        // Given
        val userId = 1L
        val expectedPairs = listOf(
            QuickAccessPairsEntity(1L, "USD", "EUR", userId),
            QuickAccessPairsEntity(2L, "GBP", "JPY", userId)
        )

        coEvery { quickAccessPairsDao.getQuickAccessPairs(userId) } returns expectedPairs

        // When
        val result = repository.getQuickAccessPairs(userId)

        // Then
        assertEquals(expectedPairs, result)
        coVerify { quickAccessPairsDao.getQuickAccessPairs(userId) }
    }

    @Test
    fun `saveQuickAccessPair should call DAO`() = runTest {
        // Given
        val pair = QuickAccessPairsEntity(
            fromCurrency = "USD",
            toCurrency = "EUR",
            userId = 1L
        )

        coEvery { quickAccessPairsDao.insertQuickAccessPair(pair) } just Runs

        // When
        repository.saveQuickAccessPair(pair)

        // Then
        coVerify { quickAccessPairsDao.insertQuickAccessPair(pair) }
    }

    @Test
    fun `incrementUsageCount should call DAO with correct pairId`() = runTest {
        // Given
        val pairId = 123L

        coEvery { quickAccessPairsDao.incrementUsageCount(pairId) } just Runs

        // When
        repository.incrementUsageCount(pairId)

        // Then
        coVerify { quickAccessPairsDao.incrementUsageCount(pairId) }
    }

    @Test
    fun `deleteQuickAccessPair should call DAO`() = runTest {
        // Given
        val pair = QuickAccessPairsEntity(
            id = 123L,
            fromCurrency = "USD",
            toCurrency = "EUR",
            userId = 1L
        )

        coEvery { quickAccessPairsDao.deleteQuickAccessPair(pair) } just Runs

        // When
        repository.deleteQuickAccessPair(pair)

        // Then
        coVerify { quickAccessPairsDao.deleteQuickAccessPair(pair) }
    }
}

// Дополнительные тесты для граничных случаев
class ExchangeRepositoryEdgeCasesTest {

    private val conversionHistoryDao = mockk<ConversionHistoryDao>()
    private val conversionRateDao = mockk<ConversionRateDao>()
    private val quickAccessPairsDao = mockk<QuickAccessPairsDao>()
    private val mockApi = mockk<CurrencyApiConfig>()

    private lateinit var repository: ExchangeRepository

    @Before
    fun setup() {
        mockkObject(RetrofitInstance)
        every { RetrofitInstance.api } returns mockApi

        repository = ExchangeRepository(
            conversionHistoryDao = conversionHistoryDao,
            conversionRateDao = conversionRateDao,
            quickAccessPairsDao = quickAccessPairsDao
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getQuickAccessPairs should handle empty list`() = runTest {
        // Given
        val userId = 1L
        val emptyList = emptyList<QuickAccessPairsEntity>()

        coEvery { quickAccessPairsDao.getQuickAccessPairs(userId) } returns emptyList

        // When
        val result = repository.getQuickAccessPairs(userId)

        // Then
        assertEquals(emptyList, result)
        coVerify { quickAccessPairsDao.getQuickAccessPairs(userId) }
    }

    @Test
    fun `convertCurrencies should handle API error response`() = runTest {
        // Given
        val accessKey = "invalid_key"
        val from = "USD"
        val to = "EUR"
        val amount = 100.0

        val errorResponse = mockk<Response<ApiResponse>> {
            every { isSuccessful } returns false
            every { code() } returns 401
        }

        coEvery {
            mockApi.convertCurrencies(accessKey, from, to, amount)
        } returns errorResponse

        // When
        val result = repository.convertCurrencies(accessKey, from, to, amount)

        // Then
        assertEquals(errorResponse, result)
        assertEquals(false, result.isSuccessful)
        assertEquals(401, result.code())
    }

    @Test
    fun `insertConversionHistory should handle large amounts`() = runTest {
        // Given
        val entity = ConversionHistoryEntity(
            fromCurrency = "USD",
            toCurrency = "EUR",
            amount = "999999999.99",
            convertedValue = "924999999.91"
        )
        val expectedId = Long.MAX_VALUE

        coEvery { conversionHistoryDao.insertTransaction(entity) } returns expectedId

        // When
        val result = repository.insertConversionHistory(entity)

        // Then
        assertEquals(expectedId, result)
    }
}