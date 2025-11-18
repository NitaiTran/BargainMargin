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
    suspend fun insertMonth(month: MonthEntity): Long // Returns the ID of the new month

    @Query("SELECT * FROM months WHERE year = :year AND month = :month LIMIT 1")
    fun getMonthWithWeeks(year: Int, month: Int): Flow<MonthWithWeeks?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeks(weeks: List<WeekEntity>)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE weekId = :weekId")
    fun getExpensesForWeek(weekId: Long): Flow<List<ExpenseEntity>>
}