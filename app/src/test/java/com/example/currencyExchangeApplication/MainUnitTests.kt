package com.example.currencyExchangeApplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.currencyExchangeApplication.data.model.ApiResponse
import com.example.currencyExchangeApplication.domain.DataUseCase
import com.example.currencyExchangeApplication.presentation.vm.MainViewModel
import org.junit.Before
import org.junit.Rule
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

class MainUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockedRepository: DataUseCase

    @Mock
    private lateinit var mockedApiResponseObserver: Observer<Response<ApiResponse>>

    @InjectMocks
    private lateinit var mainViewModel: MainViewModel


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mainViewModel = MainViewModel(mockedRepository)
        mainViewModel.myResponse.observeForever(mockedApiResponseObserver)
    }



    // Add more tests for error scenarios, edge cases, etc.

    // Make sure to test the ViewModel's behavior under different scenarios
}