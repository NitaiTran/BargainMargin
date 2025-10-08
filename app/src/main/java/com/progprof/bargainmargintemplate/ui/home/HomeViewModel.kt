package com.progprof.bargainmargintemplate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.progprof.bargainmargintemplate.myCurrentBudget
import com.progprof.bargainmargintemplate.updateRemainingBudget

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {

        value = "Budget Remaining: " + 5
    }
    val text: LiveData<String> = _text
}