package com.progprof.bargainmargintemplate.data.local.dao
import com.progprof.bargainmargintemplate.data.local.entities.BudgetEntity

import androidx.room.*
import com.progprof.bargainmargintemplate.data.local.entities.MonthEntity
import com.progprof.bargainmargintemplate.data.local.entities.WeekEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeekDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWeek(month: WeekEntity)

    @Query("SELECT * FROM budgets LIMIT 1")
    fun getBudget(): Flow<WeekEntity?>
}
