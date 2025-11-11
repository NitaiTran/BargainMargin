package com.progprof.bargainmargintemplate.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.progprof.bargainmargintemplate.data.local.AppDatabase
import com.progprof.bargainmargintemplate.data.local.entities.BudgetEntity
import com.progprof.bargainmargintemplate.data.repository.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Expense(
    val id: Int = 0,
    val amountOfExpense : Double,
    val descriptionOfExpense : String = "",
    val categoryOfExpense : String = "",
    val weekOfExpense : Int
)

data class Category(
    val id: Int = 0,
    val categoryName : String = "",
    val totalBudget : Double,
    val budgetRemaining : Double
)

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy {
        Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "budget_tracker_db"
        ).fallbackToDestructiveMigration().build()
    }

    private val repository by lazy {
        BudgetRepository(db)
    }

    private val _budgetState = MutableStateFlow(BudgetEntity())
    val budgetState: StateFlow<BudgetEntity> = _budgetState.asStateFlow()

    val expenses: StateFlow<List<Expense>> = repository.allExpenses
        .map { entityList ->
            entityList.map { entity ->
                Expense(
                    id = entity.id,
                    amountOfExpense = entity.amountOfExpense,
                    descriptionOfExpense = entity.descriptionOfExpense,
                    categoryOfExpense = entity.categoryOfExpense,
                    weekOfExpense = entity.weekOfExpense
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
        viewModelScope.launch(Dispatchers.IO) {
            repository.initializeIfEmpty()
            repository.budget
                .filterNotNull()
                .collect { budgetEntity ->
                    _budgetState.value = budgetEntity
                }
        }
    }
    fun onTotalBudgetChanged(newBudgetString: String) {
        val newTotal = newBudgetString.toDoubleOrNull() ?: 0.0
        if (newTotal <= 0) return

        viewModelScope.launch {
            val weeklyAmount = newTotal / 4
            val updatedBudget = BudgetEntity(
                id = 0,
                totalBudget = newTotal,
                monthlyRemainingBudget = newTotal,
                totalRemainingBudget = newTotal,
                week1TotalBudget = weeklyAmount,
                week1RemainingBudget = weeklyAmount,
                week2TotalBudget = weeklyAmount,
                week2RemainingBudget = weeklyAmount,
                week3TotalBudget = weeklyAmount,
                week3RemainingBudget = weeklyAmount,
                week4TotalBudget = weeklyAmount,
                week4RemainingBudget = weeklyAmount,
                myCurrentWeek = 1
            )
            repository.updateBudget(updatedBudget)
        }
    }

    fun addExpense(amount: Double, description: String, category: String, week: Int) {
        if (amount <= 0) return
        viewModelScope.launch {
            val newExpenseEntity = com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity(
                amountOfExpense = amount,
                descriptionOfExpense = description,
                categoryOfExpense = category,
                weekOfExpense = week
            )
            repository.insertExpense(newExpenseEntity)

            val currentBudget = _budgetState.value
            val updatedBudget = currentBudget.copy(
                monthlyRemainingBudget = currentBudget.monthlyRemainingBudget - amount,
                week1RemainingBudget = if (week == 1) currentBudget.week1RemainingBudget - amount else currentBudget.week1RemainingBudget,
                week2RemainingBudget = if (week == 2) currentBudget.week2RemainingBudget - amount else currentBudget.week2RemainingBudget,
                week3RemainingBudget = if (week == 3) currentBudget.week3RemainingBudget - amount else currentBudget.week3RemainingBudget,
                week4RemainingBudget = if (week == 4) currentBudget.week4RemainingBudget - amount else currentBudget.week4RemainingBudget
            )
            repository.updateBudget(updatedBudget)
        }
    }

    fun removeExpense(expense: Expense) {
        viewModelScope.launch {
            val expenseEntityToDelete = com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity(
                id = expense.id,
                amountOfExpense = expense.amountOfExpense,
                descriptionOfExpense = expense.descriptionOfExpense,
                categoryOfExpense = expense.categoryOfExpense,
                weekOfExpense = expense.weekOfExpense
            )
            repository.deleteExpense(expenseEntityToDelete)

            val currentBudget = _budgetState.value
            val amount = expense.amountOfExpense
            val updatedBudget = currentBudget.copy(
                monthlyRemainingBudget = currentBudget.monthlyRemainingBudget + amount,
                week1RemainingBudget = if (expense.weekOfExpense == 1) currentBudget.week1RemainingBudget + amount else currentBudget.week1RemainingBudget,
                week2RemainingBudget = if (expense.weekOfExpense == 2) currentBudget.week2RemainingBudget + amount else currentBudget.week2RemainingBudget,
                week3RemainingBudget = if (expense.weekOfExpense == 3) currentBudget.week3RemainingBudget + amount else currentBudget.week3RemainingBudget,
                week4RemainingBudget = if (expense.weekOfExpense == 4) currentBudget.week4RemainingBudget + amount else currentBudget.week4RemainingBudget
            )
            repository.updateBudget(updatedBudget)
        }
    }

    fun changeCurrentWeek(weekNum: Int) {
        if (weekNum !in 1..4) return
        viewModelScope.launch {
            val currentBudget = _budgetState.value
            if (currentBudget.myCurrentWeek != weekNum) {
                repository.updateBudget(currentBudget.copy(myCurrentWeek = weekNum))
            }
        }
    }

    fun alterWeeklyBudgets(week1: Double, week2: Double, week3: Double, week4: Double) {
        viewModelScope.launch {
            val currentBudget = _budgetState.value
            val newTotalBudget = week1 + week2 + week3 + week4

            val updatedBudget = currentBudget.copy(
                totalBudget = newTotalBudget,
                monthlyRemainingBudget = newTotalBudget,
                totalRemainingBudget = newTotalBudget,
                week1TotalBudget = week1,
                week1RemainingBudget = week1,
                week2TotalBudget = week2,
                week2RemainingBudget = week2,
                week3TotalBudget = week3,
                week3RemainingBudget = week3,
                week4TotalBudget = week4,
                week4RemainingBudget = week4
            )
            repository.updateBudget(updatedBudget)
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
}
