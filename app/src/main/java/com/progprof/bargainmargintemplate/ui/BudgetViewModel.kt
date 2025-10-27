package com.progprof.bargainmargintemplate.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


data class Expense(
    val amountOfExpense : Double,
    val descriptionOfExpense : String = "",
    val categoryOfExpense : String = ""
)
class BudgetViewModel : ViewModel() {

    var totalBudget by mutableStateOf("") // Text the user types into the TextField
    var monthlyRemainingBudget by mutableDoubleStateOf(0.0)
    var totalRemainingBudget by mutableDoubleStateOf(0.0)
    var categories by mutableStateOf("") //User states number of categories to split into
    val weeklyBudget :  Double
        get() = if (monthlyRemainingBudget > 0) monthlyRemainingBudget / 4.0 else 0.0
    var myNumberOfCategories by mutableIntStateOf(0) //User states number of categories to split into

    var weeklyRemainingBudget = 0.0
        //private set

    var expenses by mutableStateOf(listOf<Expense>())

    fun setInitialRemainingBudget() {
        monthlyRemainingBudget = totalBudget.toDoubleOrNull() ?: 0.0 //converts totalBudget input to double for monthBudget
    }
    fun setInitialTotalBudget() {
        totalRemainingBudget = totalBudget.toDoubleOrNull() ?: 0.0 //converts totalBudget input to double for monthBudget
    }

    fun changeBudgetLimit()
    {
        if(monthlyRemainingBudget <= 0.0)
        {
            setInitialRemainingBudget()
        }
        setInitialTotalBudget()


        if(monthlyRemainingBudget > totalRemainingBudget)
        {
            monthlyRemainingBudget = totalRemainingBudget
        }
    }

    fun setCategories() {
        myNumberOfCategories = categories.toIntOrNull() ?: 1 //converts categories input to int for myNumberOfCategories
    }

    fun calculateWeeklyBudget()
    {

    }

    fun addExpense(amount: Double, description: String = "", category: String = "") {
        if (amount <= 0) return // simple validation

        val newExpense = Expense(amount, description, category)
        expenses = expenses + newExpense // add to the list
        monthlyRemainingBudget -= amount // update remaining budget
    }
    fun removeExpense(expense: Expense) {
        expenses = expenses - expense
        monthlyRemainingBudget += expense.amountOfExpense
    }


}