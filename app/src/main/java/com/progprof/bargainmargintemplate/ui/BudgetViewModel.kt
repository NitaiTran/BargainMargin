package com.progprof.bargainmargintemplate.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BudgetViewModel : ViewModel() {

    var totalBudget by mutableStateOf("")
    var remainingBudget by mutableDoubleStateOf(1000.0)
    var monthBudget by mutableDoubleStateOf(1000.0)
    var weekBudget = (monthBudget / 4)



        private set

    fun calculateWeeklyBudget()
    {

    }
}