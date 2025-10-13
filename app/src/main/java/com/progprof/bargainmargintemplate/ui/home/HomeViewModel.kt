package com.progprof.bargainmargintemplate.ui.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.ProgressBar
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.progprof.bargainmargintemplate.myBudget
import java.lang.reflect.Modifier
import androidx.activity.ComponentActivity


class HomeViewModel : ViewModel() {
    val budgetHomeViewModel = myBudget(75, 100)

    private val _text = MutableLiveData<String>().apply {

        value = "Budget Remaining: " + budgetHomeViewModel.remainingBudget + "/" + budgetHomeViewModel.totalBudget
    }
    val text: LiveData<String>
        get() = _text


    private val _progress = MutableLiveData<Int>().apply{
        value = budgetHomeViewModel.remainingBudget

    }
    val progressValue: LiveData<Int>
        get() = _progress

    private val _max = MutableLiveData<Int>().apply{
        value = budgetHomeViewModel.totalBudget

    }
    val maxValue: LiveData<Int>
        get() = _max

    fun updateProgressBar()
    {
        updateProgress(budgetHomeViewModel.remainingBudget / budgetHomeViewModel.totalBudget)
    }

    fun updateProgress(progress: Int) {
        _progress.value = progress

    }


}

