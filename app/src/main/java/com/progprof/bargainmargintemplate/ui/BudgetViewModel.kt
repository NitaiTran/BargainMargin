package com.progprof.bargainmargintemplate.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.progprof.bargainmargintemplate.data.local.AppDatabase
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity
import com.progprof.bargainmargintemplate.data.local.entities.MonthEntity
import com.progprof.bargainmargintemplate.data.local.entities.WeekEntity
import com.progprof.bargainmargintemplate.data.local.relations.MonthWithWeeks
import com.progprof.bargainmargintemplate.data.repository.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

data class Expense(
    val id: Long = 0,
    val weekId: Long,
    val amountOfExpense: Double,
    val descriptionOfExpense: String = "",
    val categoryOfExpense: String = ""
)
data class Category(
    val id: Int = 0,
    val categoryName: String = "",
    val totalBudget: Double,
    val budgetRemaining: Double
)
data class BudgetUiState(
    val monthWithWeeks: MonthWithWeeks? = null,
    val expensesForCurrentWeek: List<Expense> = emptyList(),
    val currentWeekNumber: Int = 1
)
class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy {
        Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "budget_tracker_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    private val repository by lazy { BudgetRepository(db) }
    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()
    val categories: StateFlow<List<Category>> = repository.allCategories
        .map { entityList ->
            entityList.map { entity ->
                Category(
                    id = entity.id,
                    categoryName = entity.categoryName,
                    totalBudget = entity.totalBudget,
                    budgetRemaining = entity.budgetRemaining
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentMonthData(currentYear, currentMonth)
                .distinctUntilChanged()
                .collect { monthWithWeeks ->
                    _uiState.update { it.copy(monthWithWeeks = monthWithWeeks) }

                    val currentWeekNumber = _uiState.value.currentWeekNumber
                    val currentWeek = monthWithWeeks?.weeks?.find { it.weekNumber == currentWeekNumber }

                    if (currentWeek != null) {
                        fetchExpensesForWeek(currentWeek.id)
                    } else {
                        _uiState.update { it.copy(expensesForCurrentWeek = emptyList()) }
                    }
                }
        }
    }

    private fun fetchExpensesForWeek(weekId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getExpensesForWeek(weekId).collect { expenseEntities ->
                val expenses = expenseEntities.map {
                    Expense(
                        id = it.id,
                        weekId = it.weekId,
                        amountOfExpense = it.amountOfExpense,
                        descriptionOfExpense = it.descriptionOfExpense,
                        categoryOfExpense = it.categoryOfExpense
                    )
                }
                _uiState.update { it.copy(expensesForCurrentWeek = expenses) }
            }
        }
    }

    fun changeCurrentWeek(weekNum: Int) {
        if (weekNum !in 1..4) return
        _uiState.update { it.copy(currentWeekNumber = weekNum) }

        // After changing the week number, we need to fetch the expenses for that new week.
        val currentWeek = _uiState.value.monthWithWeeks?.weeks?.find { it.weekNumber == weekNum }
        if (currentWeek != null) {
            fetchExpensesForWeek(currentWeek.id)
        } else {
            _uiState.update { it.copy(expensesForCurrentWeek = emptyList()) }
        }
    }

    fun createNewMonthBudget(totalBudget: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val weeklyAmount = totalBudget / 4

            val newMonth = MonthEntity(year = year, month = month, totalBudget = totalBudget)
            val weeklyBudgets = listOf(weeklyAmount, weeklyAmount, weeklyAmount, weeklyAmount)

            repository.createNewMonthWithWeeks(newMonth, weeklyBudgets)
        }
    }

    fun addExpense(amount: Double, description: String, category: String) {
        val currentState = _uiState.value
        val month = currentState.monthWithWeeks?.month
        val week = currentState.monthWithWeeks?.weeks?.find { it.weekNumber == currentState.currentWeekNumber }

        if (amount <= 0 || month == null || week == null) return

        viewModelScope.launch(Dispatchers.IO) {
            val newExpenseEntity = ExpenseEntity(
                weekId = week.id,
                amountOfExpense = amount,
                descriptionOfExpense = description,
                categoryOfExpense = category
            )
            repository.insertExpenseAndUpdateTotals(newExpenseEntity, month, week)
        }
    }


    fun addCategory(categoryName: String, totalBudget: Double) {
        viewModelScope.launch {
            val newCategoryEntity = com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity(
                categoryName = categoryName,
                totalBudget = totalBudget,
                budgetRemaining = totalBudget
            )
            repository.insertCategory(newCategoryEntity)
        }
    }

    fun removeCategory(category: Category) {
        viewModelScope.launch {
            val categoryEntity = com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity(
                id = category.id,
                categoryName = category.categoryName,
                totalBudget = category.totalBudget,
                budgetRemaining = category.budgetRemaining
            )
            repository.deleteCategory(categoryEntity)
        }
    }

    fun updateCategory(oldCategory: Category, newCategory: Category) {
        viewModelScope.launch {
            val categoryEntity = com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity(
                id = oldCategory.id,
                categoryName = newCategory.categoryName,
                totalBudget = newCategory.totalBudget,
                budgetRemaining = newCategory.budgetRemaining
            )
            repository.updateCategory(categoryEntity)
        }
    }

    fun removeExpense(expense: Expense) {
        val currentState = _uiState.value
        val month = currentState.monthWithWeeks?.month
        val week = currentState.monthWithWeeks?.weeks?.find { it.id == expense.weekId }

        if (month == null || week == null) return

        viewModelScope.launch(Dispatchers.IO) {
            // Map the UI Expense object back to an ExpenseEntity to delete
            val expenseEntityToDelete = ExpenseEntity(
                id = expense.id,
                weekId = expense.weekId,
                amountOfExpense = expense.amountOfExpense,
                descriptionOfExpense = expense.descriptionOfExpense,
                categoryOfExpense = expense.categoryOfExpense
            )
            repository.deleteExpenseAndUpdateTotals(expenseEntityToDelete, month, week)
        }
    }

    fun alterWeeklyBudgets(week1: Double, week2: Double, week3: Double, week4: Double) {
        val currentState = _uiState.value
        val month = currentState.monthWithWeeks?.month
        val weeks = currentState.monthWithWeeks?.weeks
        if (month == null || weeks.isNullOrEmpty() || weeks.size != 4) return

        viewModelScope.launch(Dispatchers.IO) {
            val newWeeklyBudgets = listOf(week1, week2, week3, week4)
            repository.alterWeeklyBudgets(month, weeks, newWeeklyBudgets)
        }
    }

}