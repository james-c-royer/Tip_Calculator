package com.example.tipcalculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val rounding = MutableLiveData<String>("None")
    val split = MutableLiveData<String>("No")
    val total = MutableLiveData<Double>(0.0)
}