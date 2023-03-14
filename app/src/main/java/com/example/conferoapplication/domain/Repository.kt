package com.example.conferoapplication.domain

import com.example.conferoapplication.data.model.ApiResponse
import retrofit2.Response

interface Repository {
    suspend fun getResponse(): Response<ApiResponse>
}