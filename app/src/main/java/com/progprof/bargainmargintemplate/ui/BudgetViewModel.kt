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
import kotlinx.coroutines.withContext
import java.util.Calendar
import kotlin.math.exp

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
    val selectedMonthWithWeeks: MonthWithWeeks? = null,
    val expensesForCurrentWeek: List<Expense> = emptyList(),
    val currentWeekNumber: Int = 1,
    val allMonths: List<MonthEntity> = emptyList(),
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH),
    val isAddingExpense: Boolean = false,
    val currentMonthGoal: Double = 0.0,
    val currentWeekGoal: Double = 0.0
)

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy {
        Room.databaseBuilder(application.applicationContext, AppDatabase::class.java, "budget_tracker_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    private val repository by lazy { BudgetRepository(db) }
    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    val categories: StateFlow<List<Category>> = repository.allCategories
        .map { entityList -> entityList.map { Category(it.id, it.categoryName, it.totalBudget, it.budgetRemaining) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        val yearAndMonthFlow = _uiState.map { Pair(it.selectedYear, it.selectedMonth) }.distinctUntilChanged()

        viewModelScope.launch(Dispatchers.IO) {
            yearAndMonthFlow.flatMapLatest { (year, month) ->
                repository.getMonthWithWeeks(year, month)
            }.collect { monthWithWeeks ->
                _uiState.update { it.copy(
                    selectedMonthWithWeeks = monthWithWeeks,
                    currentMonthGoal = monthWithWeeks?.month?.monthlyGoal ?: 0.0) }
            }
        }

        val weekIdFlow = _uiState.map {
            it.selectedMonthWithWeeks?.weeks?.find { week -> week.weekNumber == it.currentWeekNumber }?.id
        }.distinctUntilChanged()

        viewModelScope.launch(Dispatchers.IO) {
            weekIdFlow.flatMapLatest { weekId ->
                if (weekId == null) {
                    flowOf(emptyList())
                } else {
                    repository.getExpensesForWeek(weekId)
                }
            }.collect { expenseEntities ->
                val expenses = expenseEntities.map {
                    Expense(it.id, it.weekId, it.amountOfExpense, it.descriptionOfExpense, it.categoryOfExpense)
                }
                _uiState.update {
                    val currentWeek = it.selectedMonthWithWeeks?.weeks?.find { week -> week.weekNumber == it.currentWeekNumber }
                    it.copy(expensesForCurrentWeek = expenses,
                        currentWeekGoal = currentWeek?.weeklyGoal ?: 0.0) }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMonths().collectLatest { allMonths ->
                _uiState.update { it.copy(allMonths = allMonths) }
                if (allMonths.isEmpty()) {
                    prepopulateYearAndFetch()
                }
            }
        }
    }

    fun changeSelectedMonth(year: Int, month: Int) {
        _uiState.update { it.copy(selectedYear = year, selectedMonth = month, currentWeekNumber = 1) }
    }

    fun changeCurrentWeek(weekNum: Int) {
        if (weekNum !in 1..4) return
        _uiState.update { it.copy(currentWeekNumber = weekNum) }
    }

    private suspend fun prepopulateYearAndFetch() {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val months = (0..11).map { month ->
            MonthEntity(year = year, month = month, totalBudget = 0.0)
        }
        repository.insertMultipleMonths(months)
    }

    fun createNewMonthBudget(totalBudget: Double, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val year = _uiState.value.selectedYear
            val month = _uiState.value.selectedMonth
            val weeklyAmount = totalBudget / 4
            val existingMonth = _uiState.value.selectedMonthWithWeeks?.month
            val monthToUpsert = existingMonth?.copy(totalBudget = totalBudget) ?: MonthEntity(year = year, month = month, totalBudget = totalBudget)
            val weeklyBudgets = listOf(weeklyAmount, weeklyAmount, weeklyAmount, weeklyAmount)
            repository.createNewMonthWithWeeks(monthToUpsert, weeklyBudgets)
            withContext(Dispatchers.Main) { onComplete() }
        }
    }

    fun addExpense(amount: Double, description: String, category: String, weekNumber: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { it.copy(isAddingExpense = true) }
                val currentState = _uiState.value
                val week = currentState.selectedMonthWithWeeks?.weeks?.find { it.weekNumber == weekNumber }
                if (amount > 0 && week != null) {
                    val newExpenseEntity = ExpenseEntity(
                        weekId = week.id,
                        amountOfExpense = amount,
                        descriptionOfExpense = description,
                        categoryOfExpense = category
                    )
                    repository.insertExpenseAndUpdateTotals(newExpenseEntity)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(isAddingExpense = false) }
                }
            }
        }
    }

    fun removeExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value
            if (currentState.selectedMonthWithWeeks == null) return@launch

            val expenseEntityToDelete = ExpenseEntity(
                id = expense.id,
                weekId = expense.weekId,
                amountOfExpense = expense.amountOfExpense,
                descriptionOfExpense = expense.descriptionOfExpense,
                categoryOfExpense = expense.categoryOfExpense
            )
            repository.deleteExpenseAndUpdateTotals(expenseEntityToDelete)
        }
    }

    fun alterWeeklyBudgets(week1: Double, week2: Double, week3: Double, week4: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value
            val month = currentState.selectedMonthWithWeeks?.month
            val weeks = currentState.selectedMonthWithWeeks?.weeks
            if (month == null || weeks.isNullOrEmpty() || weeks.size != 4) return@launch
            val newWeeklyBudgets = listOf(week1, week2, week3, week4)
            repository.alterWeeklyBudgets(month, weeks, newWeeklyBudgets)
        }
    }

    fun addCategory(categoryName: String, totalBudget: Double) {
        viewModelScope.launch {
            val newCategoryEntity = com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity(categoryName = categoryName, totalBudget = totalBudget, budgetRemaining = totalBudget)
            repository.insertCategory(newCategoryEntity)
        }
    }
    fun removeCategory(category: Category) {
        viewModelScope.launch {
            val categoryEntity = com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity(id = category.id, categoryName = category.categoryName, totalBudget = category.totalBudget, budgetRemaining = category.budgetRemaining)
            repository.deleteCategory(categoryEntity)
        }
    }
    fun updateCategory(oldCategory: Category, newCategory: Category) {
        viewModelScope.launch {
            val categoryEntity = com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity(id = oldCategory.id, categoryName = newCategory.categoryName, totalBudget = newCategory.totalBudget, budgetRemaining = newCategory.budgetRemaining)
            repository.updateCategory(categoryEntity)
        }
    }

    fun findCategoryByName(categoryName: String) : Category? {
        return categories.value.find { it.categoryName == categoryName }
    }

    // NOTE: amount can be positive or negative, if we want to remove from the budget pass a negative value
    fun updateCategoryRemainingBudget(category: Category, amount: Double) {
        viewModelScope.launch {
            val categoryEntity = com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity(id = category.id, categoryName = category.categoryName, totalBudget = category.totalBudget, budgetRemaining = category.budgetRemaining + amount)
            repository.updateCategory(categoryEntity)
        }
    }
    fun updateMonthGoal(newGoal: Double) {
        viewModelScope.launch (Dispatchers.IO){
            val currentMonth = _uiState.value.selectedMonthWithWeeks?.month ?: return@launch
            if(newGoal >= 0){
                val updatedMonth = currentMonth.copy(monthlyGoal = newGoal)
                repository.updateMonth(updatedMonth)
            }
        }
    }

    fun updateWeeklyGoal(weekNumber: Int, newGoal: Double) {
        viewModelScope.launch (Dispatchers.IO){
            val weekToUpdate = _uiState.value.selectedMonthWithWeeks?.weeks?.find {it.weekNumber == weekNumber} ?: return@launch
            if(newGoal >= 0){
                val updatedWeek = weekToUpdate.copy(weeklyGoal = newGoal)
                repository.updateWeek(updatedWeek)
            }
        }
    }
}
