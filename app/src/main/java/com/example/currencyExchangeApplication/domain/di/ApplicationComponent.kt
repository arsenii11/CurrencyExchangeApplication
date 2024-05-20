package com.example.currencyExchangeApplication.domain.di


import com.example.currencyExchangeApplication.domain.DomainModule
import com.example.currencyExchangeApplication.presentation.MainActivity


import dagger.Component



@Component(modules = [ DomainModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}