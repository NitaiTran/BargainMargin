package com.progprof.bargainmargintemplate.data.local.dao
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("DELETE FROM expenses")
    suspend fun clearAll()
}
