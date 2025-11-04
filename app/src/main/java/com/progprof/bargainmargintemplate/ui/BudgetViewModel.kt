package com.progprof.bargainmargintemplate.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Your Expense data class
data class Expense(
    val amountOfExpense : Double,
    val descriptionOfExpense : String = "",
    val categoryOfExpense : String = "",
    val weekOfExpense : Int
)

data class Category(
    val categoryName : String = "",
    val categoryBudget : Double
)

class BudgetViewModel : ViewModel() {

    // --- CHANGE: All `mutableStateOf` are now `StateFlow`s ---
    // This removes all UI dependencies from the ViewModel.

    // Private, mutable flows that only the ViewModel can modify.
    private val _totalBudget = MutableStateFlow("")
    private val _monthlyRemainingBudget = MutableStateFlow(0.0)
    private val _totalRemainingBudget = MutableStateFlow(0.0)
    private val _categories = MutableStateFlow("")
    private val _categoryList = MutableStateFlow(listOf<Category>())
    private val _myNumberOfCategories = MutableStateFlow(1) // Default to 1
    private val _expenses = MutableStateFlow(listOf<Expense>())
    private val _myCurrentWeek = MutableStateFlow(1)

    // Public, read-only flows for the UI to observe.
    val totalBudget = _totalBudget.asStateFlow()
    val monthlyRemainingBudget = _monthlyRemainingBudget.asStateFlow()
    val totalRemainingBudget = _totalRemainingBudget.asStateFlow()
    val categories = _categories.asStateFlow()
    val categoryList = _categoryList.asStateFlow()
    val myNumberOfCategories = _myNumberOfCategories.asStateFlow()
    val expenses = _expenses.asStateFlow()
    val myCurrentWeek = _myCurrentWeek.asStateFlow()

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
        }
    }

    fun settingUpVariables() {
        // Ensure at least 1 category to prevent division by zero
        _myNumberOfCategories.value = _categories.value.toIntOrNull()?.coerceAtLeast(1) ?: 1
    }

    fun changeCurrentWeek(weekNum: Int) {
        _myCurrentWeek.value = weekNum
    }

    fun addExpense(amount: Double, description: String = "", category: String = "", week: Int) {
        if (amount <= 0) return
        val newExpense = Expense(amount, description, category, week)
        // Use the thread-safe `update` function
        _expenses.update { currentExpenses -> currentExpenses + newExpense }
        _monthlyRemainingBudget.update { currentBudget -> currentBudget - amount }
    }

    fun removeExpense(expense: Expense) {
        _expenses.update { currentExpenses -> currentExpenses - expense }
        _monthlyRemainingBudget.update { currentBudget -> currentBudget + expense.amountOfExpense }
    }

    fun addCategory(name: String, budget: Double) {
        _categoryList.update { oldList -> oldList + Category(name, budget) }
    }
}
