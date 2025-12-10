package com.progprof.bargainmargintemplate.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.progprof.bargainmargintemplate.data.local.AppDatabase
import com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity
import com.progprof.bargainmargintemplate.data.local.entities.CategorySpendingHistoryEntity
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity
import com.progprof.bargainmargintemplate.data.local.entities.MonthEntity
import com.progprof.bargainmargintemplate.data.local.relations.MonthWithWeeks
import com.progprof.bargainmargintemplate.data.repository.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

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

    val currentMonthSnapshots = MutableStateFlow<List<CategorySpendingHistoryEntity>>(emptyList())

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
        // Monitor categories for low budget warnings
        viewModelScope.launch {
            categories.collect { categoryList ->
                checkCategoryBudgets(categoryList)
            }
        }

    }

    private fun checkCategoryBudgets(categoryList: List<Category>) {
        categoryList.forEach { category ->
            if (category.totalBudget > 0) {
                val percentageRemaining = (category.budgetRemaining / category.totalBudget) * 100

                when {
                    percentageRemaining <= 10 && category.budgetRemaining > 0 -> {
                        BudgetNotificationManager.sendNotification(
                            getApplication(),
                            " ${category.categoryName} Budget Critical",
                            "Only $${String.format(Locale.US, "%.2f", category.budgetRemaining)} remaining (${String.format(Locale.US, "%.0f", percentageRemaining)}%)"
                        )
                    }
                    percentageRemaining <= 25 && percentageRemaining > 10 -> {
                        BudgetNotificationManager.sendNotification(
                            getApplication(),
                            " ${category.categoryName} Budget Low",
                            "Only $${String.format(Locale.US, "%.2f", category.budgetRemaining)} remaining (${String.format(Locale.US, "%.0f", percentageRemaining)}%)"
                        )
                    }
                    category.budgetRemaining <= 0 -> {
                        BudgetNotificationManager.sendNotification(
                            getApplication(),
                            " ${category.categoryName} Budget Exceeded",
                            "You've exceeded your budget by $${String.format(Locale.US, "%.2f",
                                abs(category.budgetRemaining)
                            )}"
                        )
                    }
                }
            }
        }
    }

    private fun checkWeeklyBudget(weekSpent: Double, weekBudget: Double, weekNumber: Int) {
        if (weekBudget > 0) {
            val percentageUsed = (weekSpent / weekBudget) * 100
            val remaining = weekBudget - weekSpent

            when {
                percentageUsed >= 100 -> {
                    BudgetNotificationManager.sendNotification(
                        getApplication(),
                        "Week $weekNumber Budget Exceeded",
                        "You've spent $${String.format(Locale.US, "%.2f", weekSpent)} of $${String.format(Locale.US, "%.2f", weekBudget)}"
                    )
                }
                percentageUsed >= 75 && percentageUsed < 100 -> {
                    BudgetNotificationManager.sendNotification(
                        getApplication(),
                        "Week $weekNumber Budget Alert",
                        "You've used ${String.format(Locale.US, "%.0f", percentageUsed)}% of your budget. $${String.format(Locale.US, "%.2f", remaining)} remaining"
                    )
                }
            }


        }
    }

    private fun checkMonthlyBudget(monthSpent: Double, monthBudget: Double) {
        if (monthBudget > 0) {
            val percentageUsed = (monthSpent / monthBudget) * 100
            val remaining = monthBudget - monthSpent

            when {
                percentageUsed >= 100 -> {
                    BudgetNotificationManager.sendNotification(
                        getApplication(),
                        "Monthly Budget Exceeded",
                        "You've spent $${String.format(Locale.US, "%.2f", monthSpent)} of $${String.format(Locale.US, "%.2f", monthBudget)}"
                    )
                }
                percentageUsed >= 90 && percentageUsed < 100 -> {
                    BudgetNotificationManager.sendNotification(
                        getApplication(),
                        "Monthly Budget Critical",
                        "You've used ${String.format(Locale.US, "%.0f", percentageUsed)}% of your budget. $${String.format(Locale.US, "%.2f", remaining)} remaining"
                    )
                }
                percentageUsed >= 75 -> {
                    BudgetNotificationManager.sendNotification(
                        getApplication(),
                        "Monthly Budget Alert",
                        "You've used ${String.format(Locale.US, "%.0f", percentageUsed)}% of your budget. $${String.format(Locale.US, "%.2f", remaining)} remaining"
                    )
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

                    // Check weekly budget
                    val totalWeekSpent = currentState.expensesForCurrentWeek.sumOf { it.amountOfExpense } + amount
                    checkWeeklyBudget(totalWeekSpent, week.weekBudget, weekNumber)

                    // Check monthly budget - calculate total across all weeks
                    var monthTotalSpent = 0.0
                    currentState.selectedMonthWithWeeks.weeks.forEach { w ->
                        val weekExpenses = repository.getExpensesForWeek(w.id).first()
                        monthTotalSpent += weekExpenses.sumOf { it.amountOfExpense }
                    }
                    checkMonthlyBudget(monthTotalSpent, currentState.currentMonthGoal)
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
    fun removeCategory(category: CategoryEntity) {
        viewModelScope.launch {
            val categoryEntity = com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity(id = category.id, categoryName = category.categoryName, totalBudget = category.totalBudget, budgetRemaining = category.budgetRemaining)
            repository.deleteCategory(categoryEntity)
        }
    }
    fun getTotalCategoryBudget() : Double {
        return categories.value.sumOf{it.totalBudget}
    }
    fun updateCategory(oldCategory: CategoryEntity, newCategory: CategoryEntity) {
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

    fun generateMonthlyCategorySnapshots(year: Int, month: Int) = viewModelScope.launch {
        repository.generateMonthlyCategorySnapshots(year, month)
    }

    fun loadCurrentMonthSnapshots() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)

            val snapshots = repository.getSnapshotsForMonth(year, month)
            currentMonthSnapshots.value = snapshots
        }
    }


}
