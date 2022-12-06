package com.example.conferoapplication

import com.example.conferoapplication.service.CurrenciesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.getgeoapi.com/v2/currency/list/")
            .build()
    }

    @Provides
    fun provideExchangeService(retrofit: Retrofit): CurrenciesService {
        return retrofit.create(CurrenciesService::class.java)
    }

}