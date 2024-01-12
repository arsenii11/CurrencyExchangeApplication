package com.example.currencyExchangeApplication.DI

import androidx.lifecycle.ViewModel
import com.example.currencyExchangeApplication.presentation.vm.MainViewModel
import dagger.Binds
import dagger.Module


@Module
interface ViewModelModule {


    @Binds
    fun bindMainViewModel(impl: MainViewModel):ViewModel


}