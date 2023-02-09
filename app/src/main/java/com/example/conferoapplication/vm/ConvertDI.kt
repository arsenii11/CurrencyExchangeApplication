package com.example.conferoapplication.vm

import com.example.conferoapplication.Utilities.Resource
import com.example.conferoapplication.model.ApiResponse
import com.example.conferoapplication.repo.ExchangeDataSource
import com.example.conferoapplication.data.service.BaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class ConvertDI @Inject constructor(private val apiDataSource: ExchangeDataSource) : BaseDataSource() {
    suspend fun getConvertedData(access_key: String, from: String, to: String, amount: Double): Flow<Resource<ApiResponse>> {
        return flow {
            emit(safeApiCall { apiDataSource.getConvertedRate(access_key, from, to, amount) })
        }.flowOn(Dispatchers.IO)
    }
}