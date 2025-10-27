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
    val categoryOfExpense : String = "",
    val weekOfExpense : Int
)
class BudgetViewModel : ViewModel() {

    var totalBudget by mutableStateOf("") // Text the user types into the TextField
    var monthlyRemainingBudget by mutableDoubleStateOf(0.0)
    var totalRemainingBudget by mutableDoubleStateOf(0.0)
    var categories by mutableStateOf("") //User states number of categories to split into
    var myNumberOfCategories by mutableIntStateOf(0) //User states number of categories to split into
    var expenses by mutableStateOf(listOf<Expense>())

    var week1RemainingBudget = 0.0
    var week1TotalBudget = 0.0

    var week2RemainingBudget = 0.0
    var week2TotalBudget = 0.0

    var week3RemainingBudget = 0.0
    var week3TotalBudget = 0.0

    var week4RemainingBudget = 0.0
    var week4TotalBudget = 0.0
    fun setInitialRemainingBudget() {
        monthlyRemainingBudget = totalBudget.toDoubleOrNull() ?: 0.0 //converts totalBudget input to double for monthBudget
    }
    fun setInitialTotalBudget() {
        totalRemainingBudget = totalBudget.toDoubleOrNull() ?: 0.0 //converts totalBudget input to double for monthBudget
    }

    fun setWeeklyInitialBudgets()
    {

        week1RemainingBudget = totalRemainingBudget / 4.0

        week2RemainingBudget = totalRemainingBudget / 4.0

        week3RemainingBudget = totalRemainingBudget / 4.0

        week4RemainingBudget = totalRemainingBudget / 4.0
    }

    fun setWeeklyTotalBudgets()
    {
        week1TotalBudget = totalRemainingBudget / 4.0
        week2TotalBudget = totalRemainingBudget / 4.0
        week3TotalBudget = totalRemainingBudget / 4.0
        week4TotalBudget = totalRemainingBudget / 4.0
    }

    fun changeBudgetLimit()
    {
        if(monthlyRemainingBudget <= 0.0)
        {
            setInitialRemainingBudget()
            setWeeklyInitialBudgets()
        }
        setInitialTotalBudget()
        setWeeklyTotalBudgets()

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
        //TODO
    }

    fun addExpense(amount: Double, description: String = "", category: String = "", week: Int) {
        if (amount <= 0) return // simple validation

        val newExpense = Expense(amount, description, category, week)
        expenses = expenses + newExpense // add to the list
        monthlyRemainingBudget -= amount // update remaining budget
    }
    fun removeExpense(expense: Expense) {
        expenses = expenses - expense
        monthlyRemainingBudget += expense.amountOfExpense
    }


}