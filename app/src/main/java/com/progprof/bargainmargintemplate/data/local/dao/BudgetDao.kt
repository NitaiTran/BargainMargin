package com.progprof.bargainmargintemplate.data.local.dao

import androidx.room.*
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity
import com.progprof.bargainmargintemplate.data.local.entities.MonthEntity
import com.progprof.bargainmargintemplate.data.local.entities.WeekEntity
import com.progprof.bargainmargintemplate.data.local.relations.MonthWithWeeks
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthAndGetId(month: MonthEntity): Long

    @Update
    suspend fun updateMonth(month: MonthEntity)

    @Update
    suspend fun updateWeek(week: WeekEntity)

    @Transaction
    @Query("SELECT * FROM months WHERE year = :year AND month = :month LIMIT 1")
    fun getMonthWithWeeks(year: Int, month: Int): Flow<MonthWithWeeks?>

    @Query("SELECT * FROM months ORDER BY year, month")
    fun getAllMonths(): Flow<List<MonthEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMultipleMonths(months: List<MonthEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeks(weeks: List<WeekEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE weekId = :weekId")
    fun getExpensesForWeek(weekId: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM weeks WHERE id = :weekId")
    suspend fun getWeekById(weekId: Long): WeekEntity?

    @Query("SELECT * FROM months WHERE id = :monthId")
    suspend fun getMonthById(monthId: Long): MonthEntity?

    @Transaction
    suspend fun addExpenseAndUpdateTotals(expense: ExpenseEntity) {
        val week = getWeekById(expense.weekId) ?: return
        val month = getMonthById(week.monthId) ?: return

        insertExpense(expense)

        val updatedWeek = week.copy(weekSpent = week.weekSpent + expense.amountOfExpense)
        val updatedMonth = month.copy(totalSpent = month.totalSpent + expense.amountOfExpense)

        updateWeek(updatedWeek)
        updateMonth(updatedMonth)
    }

    @Transaction
    suspend fun deleteExpenseAndUpdateTotals(expense: ExpenseEntity) {
        val week = getWeekById(expense.weekId) ?: return
        val month = getMonthById(week.monthId) ?: return

        deleteExpenseById(expense.id)

        val updatedWeek = week.copy(weekSpent = week.weekSpent - expense.amountOfExpense)
        val updatedMonth = month.copy(totalSpent = month.totalSpent - expense.amountOfExpense)

        updateWeek(updatedWeek)
        updateMonth(updatedMonth)
    }

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: Long)
}
