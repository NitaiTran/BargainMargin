package com.progprof.bargainmargintemplate.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BudgetViewModel : ViewModel() {

    var totalBudget by mutableStateOf("") // Text the user types into the TextField
    var monthlyRemainingBudget by mutableDoubleStateOf(0.0)
    var categories by mutableStateOf("") //User states number of categories to split into

    val weeklyBudget :  Double
        get() = if (monthlyRemainingBudget > 0) monthlyRemainingBudget / 4.0 else 0.0
        //private set


    fun setInitialBudget() {
        monthlyRemainingBudget = totalBudget.toDoubleOrNull() ?: 0.0 //converts totalBudget input to double for monthBudget
    }



    fun calculateWeeklyBudget()
    {

    }
}