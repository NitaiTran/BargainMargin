package com.progprof.bargainmargintemplate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.room.Database
import androidx.room.RoomDatabase

@Entity(tableName = "budget_table")
data class PersistentData (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    //val DBMonthlyTotalBudget: Double?,
    //val DBMonthlyRemainingBudget: Double?,
    @ColumnInfo(name = "MonthlyTotalBudget") val DBMonthlyTotalBudget: Double?,
    @ColumnInfo(name = "MonthlyRemainingBudget") val DBMonthlyRemainingBudget: Double?

)

@Dao
interface BudgetDao {
    @Insert
    suspend fun insert(budget: PersistentData)

    @get:Query("SELECT * FROM budget_table")
    val myData: Flow<List<PersistentData>>
}

@Database(entities = [PersistentData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
}