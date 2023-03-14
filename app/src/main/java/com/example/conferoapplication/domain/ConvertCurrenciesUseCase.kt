package com.example.conferoapplication.domain

import com.example.conferoapplication.data.model.ApiResponse
import retrofit2.Response
import javax.inject.Inject

class ConvertCurrenciesUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke():Response<ApiResponse>{
        return repository.getResponse()
    }
}