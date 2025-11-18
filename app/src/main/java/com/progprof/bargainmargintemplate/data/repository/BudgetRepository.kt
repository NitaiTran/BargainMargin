package com.progprof.bargainmargintemplate.data.repository

import com.progprof.bargainmargintemplate.data.local.AppDatabase
import com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity
import com.progprof.bargainmargintemplate.data.local.entities.MonthEntity
import com.progprof.bargainmargintemplate.data.local.entities.WeekEntity
import com.progprof.bargainmargintemplate.data.local.relations.MonthWithWeeks
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class BudgetRepository(private val db: AppDatabase) {

    private val budgetDao = db.budgetDao()
    private val categoryDao = db.categoryDao()

    fun getCurrentMonthData(year: Int, month: Int): Flow<MonthWithWeeks?> {
        return budgetDao.getMonthWithWeeks(year, month)
    }

    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    fun getExpensesForWeek(weekId: Long): Flow<List<ExpenseEntity>> {
        return budgetDao.getExpensesForWeek(weekId)
    }

    suspend fun createNewMonthWithWeeks(monthEntity: MonthEntity, weeklyBudgets: List<Double>) {
        val monthId = budgetDao.insertMonth(monthEntity)

        val weekEntities = weeklyBudgets.mapIndexed { index, budget ->
            WeekEntity(
                monthId = monthId,
                weekNumber = index + 1,
                weekBudget = budget,
                weekSpent = 0.0
            )
        }
        budgetDao.insertWeeks(weekEntities)
    }

    suspend fun deleteExpenseAndUpdateTotals(expense: ExpenseEntity, month: MonthEntity, week: WeekEntity) {
        budgetDao.deleteExpense(expense)

        val updatedWeek = week.copy(weekSpent = week.weekSpent - expense.amountOfExpense)
        budgetDao.insertWeeks(listOf(updatedWeek))

        // Subtract the spent amount from the month
        val updatedMonth = month.copy(totalSpent = month.totalSpent - expense.amountOfExpense)
        budgetDao.insertMonth(updatedMonth)
    }

    suspend fun alterWeeklyBudgets(month: MonthEntity, currentWeeks: List<WeekEntity>, newWeeklyBudgets: List<Double>) {
        val newTotalBudget = newWeeklyBudgets.sum()
        val updatedMonth = month.copy(totalBudget = newTotalBudget)
        budgetDao.insertMonth(updatedMonth)

        val updatedWeeks = currentWeeks.mapIndexed { index, week ->
            week.copy(weekBudget = newWeeklyBudgets[index])
        }
        budgetDao.insertWeeks(updatedWeeks)
    }

    suspend fun insertExpenseAndUpdateTotals(expense: ExpenseEntity, month: MonthEntity, week: WeekEntity) {
        budgetDao.insertExpense(expense)
        val updatedWeek = week.copy(weekSpent = week.weekSpent + expense.amountOfExpense)
        budgetDao.insertWeeks(listOf(updatedWeek))
        val updatedMonth = month.copy(totalSpent = month.totalSpent + expense.amountOfExpense)
        budgetDao.insertMonth(updatedMonth)
    }
    suspend fun insertCategory(categoryEntity: CategoryEntity) {
        categoryDao.insertCategory(categoryEntity)
    }

    suspend fun deleteCategory(categoryEntity: CategoryEntity) {
        categoryDao.deleteCategory(categoryEntity)
    }

    suspend fun updateCategory(categoryEntity: CategoryEntity) {
        categoryDao.updateCategory(categoryEntity)
    }
}