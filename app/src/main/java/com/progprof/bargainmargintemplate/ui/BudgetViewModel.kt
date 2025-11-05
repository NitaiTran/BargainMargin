package com.progprof.bargainmargintemplate.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.text.toDoubleOrNull

// Your Expense data class
data class Expense(
    val amountOfExpense : Double,
    val descriptionOfExpense : String = "",
    val categoryOfExpense : String = "",
    val weekOfExpense : Int
)

class BudgetViewModel : ViewModel() {

    // --- CHANGE: All `mutableStateOf` are now `StateFlow`s ---
    // This removes all UI dependencies from the ViewModel.

    // Private, mutable flows that only the ViewModel can modify.
    private val _totalBudget = MutableStateFlow("")
    private val _monthlyRemainingBudget = MutableStateFlow(0.0)
    private val _totalRemainingBudget = MutableStateFlow(0.0)
    private val _categories = MutableStateFlow("")
    private val _myNumberOfCategories = MutableStateFlow(1) // Default to 1
    private val _expenses = MutableStateFlow(listOf<Expense>())
    private val _myCurrentWeek = MutableStateFlow(1)
    private var _week1RemainingBudget = MutableStateFlow(0.0)
    private var _week1TotalBudget = MutableStateFlow(0.0)
    private var _week2RemainingBudget = MutableStateFlow(0.0)
    private var _week2TotalBudget = MutableStateFlow(0.0)
    private var _week3RemainingBudget = MutableStateFlow(0.0)
    private var _week3TotalBudget = MutableStateFlow(0.0)
    private var _week4RemainingBudget = MutableStateFlow(0.0)
    private var _week4TotalBudget = MutableStateFlow(0.0)


    // Public, read-only flows for the UI to observe.
    val totalBudget = _totalBudget.asStateFlow()
    val monthlyRemainingBudget = _monthlyRemainingBudget.asStateFlow()
    val totalRemainingBudget = _totalRemainingBudget.asStateFlow()
    val categories = _categories.asStateFlow()
    val myNumberOfCategories = _myNumberOfCategories.asStateFlow()
    val expenses = _expenses.asStateFlow()
    val myCurrentWeek = _myCurrentWeek.asStateFlow()
    val week1RemainingBudget = _week1RemainingBudget.asStateFlow()
    val week1TotalBudget = _week1TotalBudget.asStateFlow()
    val week2RemainingBudget = _week2RemainingBudget.asStateFlow()
    val week2TotalBudget = _week2TotalBudget.asStateFlow()
    val week3RemainingBudget = _week3RemainingBudget.asStateFlow()
    val week3TotalBudget = _week3TotalBudget.asStateFlow()
    val week4RemainingBudget = _week4RemainingBudget.asStateFlow()
    val week4TotalBudget = _week4TotalBudget.asStateFlow()

    fun setWeeklyInitialBudgets(newTotal: Double)
    {

        _week1RemainingBudget.value = newTotal / 4.0
        _week2RemainingBudget.value = newTotal / 4.0
        _week3RemainingBudget.value = newTotal / 4.0
        _week4RemainingBudget.value = newTotal / 4.0
    }

    fun setWeeklyTotalBudgets(newTotal: Double)
    {
        _week1TotalBudget.value = newTotal / 4.0
        _week2TotalBudget.value = newTotal / 4.0
        _week3TotalBudget.value = newTotal / 4.0
        _week4TotalBudget.value = newTotal / 4.0
    }

    // --- CHANGE: New public functions for the UI to call ---
    fun onTotalBudgetChanged(newBudget: String) {
        _totalBudget.value = newBudget
    }

    fun onCategoriesChanged(newCategories: String) {
        _categories.value = newCategories
    }

    // --- CHANGE: Business logic now uses the .value property of StateFlow ---
    fun changeBudgetLimit() {
        val newTotal = _totalBudget.value.toDoubleOrNull() ?: 0.0
        _totalRemainingBudget.value = newTotal
        // Only reset the monthly remaining if it's zero or somehow over budget
        if (_monthlyRemainingBudget.value <= 0.0 || _monthlyRemainingBudget.value > newTotal) {
            _monthlyRemainingBudget.value = newTotal
            setWeeklyTotalBudgets(newTotal)
            setWeeklyInitialBudgets(newTotal)
        }
    }


    fun getCurrentWeekTotalBudget(): Double
    {
        when (myCurrentWeek.value)
        {
            1 -> {
                return _week1TotalBudget.value
            }
            2 -> {
                return _week2TotalBudget.value
            }
            3 -> {
                return _week3TotalBudget.value
            }
            4 -> {
                return _week4TotalBudget.value
            } else -> {
            return _week1TotalBudget.value
        }
        }
    }

    fun getCurrentWeekRemainingBudget(): Double
    {
        when (myCurrentWeek.value)
        {
            1 -> {
                return _week1RemainingBudget.value
            }
            2 -> {
                return _week2RemainingBudget.value
            }
            3 -> {
                return _week3RemainingBudget.value
            }
            4 -> {
                return _week4RemainingBudget.value
            } else -> {
            return _week1RemainingBudget.value
        }
        }
    }
/*
    fun changeBudgetLimit()
    {
        if(monthlyRemainingBudget <= 0.0)
        {
            onTotalBudgetChanged()
            setWeeklyInitialBudgets()
        }

        setInitialTotalBudget()
        setWeeklyTotalBudgets()

        if(monthlyRemainingBudget > totalRemainingBudget)
        {
            monthlyRemainingBudget = totalRemainingBudget
            setWeeklyInitialBudgets()
        }
    }
 */
fun calculateWeeklyBudget(amount: Double)
{

    when (myCurrentWeek.value)
    {
        1 -> {
            _week1RemainingBudget.value -= amount
        }
        2 -> {
            _week2RemainingBudget.value -= amount
        }
        3 -> {
            _week3RemainingBudget.value -= amount
        }
        4 -> {
            _week4RemainingBudget.value -= amount
        }
    }
}
    fun settingUpVariables() {
        // Ensure at least 1 category to prevent division by zero
        _myNumberOfCategories.value = _categories.value.toIntOrNull()?.coerceAtLeast(1) ?: 1
    }

    fun changeCurrentWeek(weekNum: Int) {
        _myCurrentWeek.value = weekNum
    }

    fun getCurrentWeek(): Int
    {
        return _myCurrentWeek.value
    }
    fun addExpense(amount: Double, description: String = "", category: String = "", week: Int) {
        if (amount <= 0) return
        val newExpense = Expense(amount, description, category, week)
        // Use the thread-safe `update` function
        _expenses.update { currentExpenses -> currentExpenses + newExpense }
        _monthlyRemainingBudget.update { currentBudget -> currentBudget - amount }
        calculateWeeklyBudget(amount)
    }

    fun removeExpense(expense: Expense) {
        _expenses.update { currentExpenses -> currentExpenses - expense }
        _monthlyRemainingBudget.update { currentBudget -> currentBudget + expense.amountOfExpense }
        changeCurrentWeek(expense.weekOfExpense)
        calculateWeeklyBudget(-expense.amountOfExpense)
    }
}
