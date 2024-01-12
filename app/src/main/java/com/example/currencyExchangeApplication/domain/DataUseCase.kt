package com.example.currencyExchangeApplication.domain

import com.example.currencyExchangeApplication.data.model.ApiResponse
import retrofit2.Response
import javax.inject.Inject

class DataUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    //UseCase is bound with interface implementing Dependency Inversion principle(SOLID)
    suspend operator fun invoke(accessKey: String, from: String, to: String, amount: Double): Response<ApiResponse> {
         return currencyRepository.getResponse(accessKey, from, to, amount)
    }
}