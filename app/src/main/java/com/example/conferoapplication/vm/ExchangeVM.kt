package com.example.conferoapplication.vm

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conferoapplication.repo.ExchangeDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeVM @Inject constructor(private val exchangeDataSource: ExchangeDataSource) :
    ViewModel() {


    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()


    fun loadAvailableCurrencies() = viewModelScope.launch {

    }


    val liveData = MutableLiveData<String>()


    init {
        startTimer()
    }

    fun startTimer() {
        object : CountDownTimer(20000,1000){
            override fun onFinish() {

            }

            override fun onTick(p0:Long) {
                liveData.value = (p0/1000).toString()
            }
        }.start()

    }


    var input_1: Float = 0.0F
    var input_2: Float = 0.0F

    val currentInput_1: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }
    val currentInput_2: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }


}