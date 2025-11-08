package com.progprof.bargainmargintemplate.data.local.dao
import com.progprof.bargainmargintemplate.data.local.entities.BudgetEntity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budgets LIMIT 1")
    fun getBudget(): Flow<BudgetEntity?>
}
