package com.example.currencyExchangeApplication.domain

import com.example.currencyExchangeApplication.data.repository.CurrencyRepositoryImpl
import com.example.currencyExchangeApplication.domain.CurrencyRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository
}