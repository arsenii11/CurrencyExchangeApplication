package com.example.currencyExchangeApplication.domain

import com.example.currencyExchangeApplication.data.repository.CurrencyRepositoryImpl
import com.example.currencyExchangeApplication.data.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository
}
