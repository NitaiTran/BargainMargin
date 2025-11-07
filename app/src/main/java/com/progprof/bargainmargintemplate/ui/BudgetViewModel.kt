package com.progprof.bargainmargintemplate.ui

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.progprof.bargainmargintemplate.data.local.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.text.toDoubleOrNull
import androidx.lifecycle.viewModelScope
import com.progprof.bargainmargintemplate.data.local.entities.BudgetEntity
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity
import com.progprof.bargainmargintemplate.data.repository.BudgetRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


// Your Expense data class
data class Expense(
    val amountOfExpense : Double,
    val descriptionOfExpense : String = "",
    val categoryOfExpense : String = "",
    val weekOfExpense : Int
)

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

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

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "budget_tracker_db"
    ).fallbackToDestructiveMigration().build()

    private val repository = BudgetRepository(db)

    val DBexpenses = repository.allExpenses.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val DBcategories = repository.allCategories.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val DBbudget = repository.budget.stateIn(viewModelScope, SharingStarted.Lazily, null)
    val budgetRepo = repository.budget.stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        viewModelScope.launch {
            repository.initializeIfEmpty()
        }
    }
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

    fun noWeeklyOverflow()
    {
        val current = budgetRepo.value ?: BudgetEntity()
        if(current.week1RemainingBudget > current.week1TotalBudget)
        {
            _week1RemainingBudget.value = _week1TotalBudget.value
        }
        if(current.week2RemainingBudget > current.week2TotalBudget)
        {
            _week2RemainingBudget.value = _week2TotalBudget.value
        }
        if(current.week3RemainingBudget > current.week3TotalBudget)
        {
            _week3RemainingBudget.value = _week3TotalBudget.value
        }
        if(current.week4RemainingBudget > current.week4TotalBudget)
        {
            _week4RemainingBudget.value = _week4TotalBudget.value
        }

    }
    // --- CHANGE: Business logic now uses the .value property of StateFlow ---
    fun changeBudgetLimit() {
        val newTotal = _totalBudget.value.toDoubleOrNull() ?: 0.0
        _totalRemainingBudget.value = newTotal
        setWeeklyTotalBudgets(newTotal)
        // Only reset the monthly remaining if it's zero or somehow over budget
        if (_monthlyRemainingBudget.value <= 0.0 || _monthlyRemainingBudget.value > newTotal) {
            _monthlyRemainingBudget.value = newTotal

            setWeeklyInitialBudgets(newTotal)
        }
        noWeeklyOverflow()
        updateDBBudget()
    }

    fun setBudgetLimit(newTotal: Double)
    {
        _totalBudget.value = newTotal.toString()
        changeBudgetLimit()
        updateDBBudget()
    }
    fun alterWeeklyBudgets(week1: Double, week2: Double,week3: Double, week4: Double)
    {
        _week1TotalBudget.value = week1
        if(_week1RemainingBudget.value > week1)
        {
            _week1RemainingBudget.value = week1
        }


        _week2TotalBudget.value = week2
        if(_week2RemainingBudget.value > week2)
        {
            _week2RemainingBudget.value = week2
        }

        _week3TotalBudget.value = week3
        if(_week3RemainingBudget.value > week3)
        {
            _week3RemainingBudget.value = week3
        }

        _week4TotalBudget.value = week4
        if(_week4RemainingBudget.value > week4)
        {
            _week4RemainingBudget.value = week4
        }
        updateDBBudget()
    }
    fun getCurrentWeekTotalBudget(): Double
    {
        val current = budgetRepo.value ?: BudgetEntity()
        when (current.myCurrentWeek)
        {
            1 -> {
                return current.week1TotalBudget
            }
            2 -> {
                return current.week2TotalBudget
            }
            3 -> {
                return current.week3TotalBudget
            }
            4 -> {
                return current.week4TotalBudget
            } else -> {
            return current.week1TotalBudget
        }
        }
    }

    fun getCurrentWeekRemainingBudget(): Double
    {
        val current = budgetRepo.value ?: BudgetEntity()
        when (current.myCurrentWeek)
        {
            1 -> {
                return current.week1RemainingBudget
            }
            2 -> {
                return current.week2RemainingBudget
            }
            3 -> {
                return current.week3RemainingBudget
            }
            4 -> {
                return current.week4RemainingBudget
            } else -> {
            return current.week1RemainingBudget
        }
        }
    }

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
        updateDBBudget()
    }
    fun settingUpVariables() {
        // Ensure at least 1 category to prevent division by zero
        _myNumberOfCategories.value = _categories.value.toIntOrNull()?.coerceAtLeast(1) ?: 1
    }

    fun changeCurrentWeek(weekNum: Int) {
        _myCurrentWeek.value = weekNum
        updateDBBudget()
    }

    fun addDBExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }
    fun updateDBBudget() {
        viewModelScope.launch {
            val current = budgetRepo.value ?: BudgetEntity()
            val updated = current.copy(
                totalBudget = totalRemainingBudget.value,
                totalRemainingBudget = totalRemainingBudget.value,
                monthlyRemainingBudget = monthlyRemainingBudget.value,
                week1TotalBudget = week1TotalBudget.value,
                week1RemainingBudget = week1RemainingBudget.value,
                week2TotalBudget = week2TotalBudget.value,
                week2RemainingBudget = week2RemainingBudget.value,
                week3TotalBudget = week3TotalBudget.value,
                week3RemainingBudget = week3RemainingBudget.value,
                week4TotalBudget = week4TotalBudget.value,
                week4RemainingBudget = week4RemainingBudget.value,
                myCurrentWeek = myCurrentWeek.value
            )
            repository.updateBudget(updated)
        }
    }

    fun addExpense(amount: Double, description: String = "", category: String = "", week: Int) {
        if (amount <= 0) return
        val newExpense = Expense(amount, description, category, week)
        val newExpenseEntity = ExpenseEntity(0,amount, description, category, week)

        // Use the thread-safe `update` function
        _expenses.update { currentExpenses -> currentExpenses + newExpense }
        _monthlyRemainingBudget.update { currentBudget -> currentBudget - amount }
        calculateWeeklyBudget(amount)
        addDBExpense(newExpenseEntity)
        updateDBBudget()
    }

    fun removeExpense(expense: Expense) {
        _expenses.update { currentExpenses -> currentExpenses - expense }
        _monthlyRemainingBudget.update { currentBudget -> currentBudget + expense.amountOfExpense }
        changeCurrentWeek(expense.weekOfExpense)
        calculateWeeklyBudget(-expense.amountOfExpense)
        updateDBBudget()
    }
}
