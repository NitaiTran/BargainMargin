package com.progprof.bargainmargintemplate.data.repository

import com.progprof.bargainmargintemplate.data.local.AppDatabase
import com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity
import com.progprof.bargainmargintemplate.data.local.entities.MonthEntity
import com.progprof.bargainmargintemplate.data.local.entities.WeekEntity
import com.progprof.bargainmargintemplate.data.local.relations.MonthWithWeeks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

import java.util.Calendar
import com.progprof.bargainmargintemplate.data.local.entities.CategorySpendingHistoryEntity


class BudgetRepository(private val db: AppDatabase) {

    private val budgetDao = db.budgetDao()
    private val categoryDao = db.categoryDao()

    private val historyDao = db.categorySpendingHistoryDao()

    fun getMonthWithWeeks(year: Int, month: Int): Flow<MonthWithWeeks?> {
        return budgetDao.getMonthWithWeeks(year, month)
    }

    fun getAllMonths(): Flow<List<MonthEntity>> {
        return budgetDao.getAllMonths()
    }

    suspend fun insertMultipleMonths(months: List<MonthEntity>) {
        budgetDao.insertMultipleMonths(months)
    }

    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    fun getExpensesForWeek(weekId: Long): Flow<List<ExpenseEntity>> {
        return budgetDao.getExpensesForWeek(weekId)
    }


    suspend fun createNewMonthWithWeeks(monthEntity: MonthEntity, weeklyBudgets: List<Double>) {
        val monthId = budgetDao.insertMonthAndGetId(monthEntity)
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

    suspend fun deleteExpenseAndUpdateTotals(expense: ExpenseEntity) {
        budgetDao.deleteExpenseAndUpdateTotals(expense)
    }

    suspend fun alterWeeklyBudgets(month: MonthEntity, currentWeeks: List<WeekEntity>, newWeeklyBudgets: List<Double>) {
        val newTotalBudget = newWeeklyBudgets.sum()
        val updatedMonth = month.copy(totalBudget = newTotalBudget)
        budgetDao.updateMonth(updatedMonth)

        val updatedWeeks = currentWeeks.mapIndexed { index, week ->
            week.copy(weekBudget = newWeeklyBudgets[index])
        }
        budgetDao.insertWeeks(updatedWeeks)
    }

    suspend fun insertExpenseAndUpdateTotals(expense: ExpenseEntity) {
        budgetDao.addExpenseAndUpdateTotals(expense)
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

    suspend fun updateMonth(month: MonthEntity) {
        budgetDao.updateMonth(month)
    }

    suspend fun updateWeek(week: WeekEntity) {
        budgetDao.updateWeek(week)
    }

    suspend fun insertCategorySpendingSnapshot(snapshot: CategorySpendingHistoryEntity) {
        historyDao.insertSnapshot(snapshot)
    }

    fun getSpendingHistoryForCategory(categoryId: Int): Flow<List<CategorySpendingHistoryEntity>> {
        return historyDao.getSpendingHistoryForCategory(categoryId)
    }

    suspend fun generateMonthlyCategorySnapshots(year: Int, month: Int) {
        // Query all categories
        val categories = categoryDao.getAllCategories().first()

        // For each category, sum expenses for given month
        categories.forEach { category ->
            val totalSpent = budgetDao.getTotalSpentByCategoryForMonth(category.categoryName, year, month)
            val snapshot = CategorySpendingHistoryEntity(
                categoryId = category.id,
                year = year,
                month = month,
                amountSpent = totalSpent,
                totalBudgetAtSnapshot = category.totalBudget
            )
            insertCategorySpendingSnapshot(snapshot)
        }
    }
    suspend fun getSnapshotsForMonth(year: Int, month: Int): List<CategorySpendingHistoryEntity> {
        return historyDao.getSnapshotsForMonth(year, month)
    }
}
