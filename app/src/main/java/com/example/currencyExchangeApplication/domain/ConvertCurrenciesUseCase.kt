package com.example.currencyExchangeApplication.domain

import com.example.currencyExchangeApplication.data.model.ApiResponse
import retrofit2.Response
import javax.inject.Inject

class ConvertCurrenciesUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke():Response<ApiResponse>{
        return repository.getResponse()
    }
}