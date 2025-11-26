package com.progprof.bargainmargintemplate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.progprof.bargainmargintemplate.data.local.dao.BudgetDao
import com.progprof.bargainmargintemplate.data.local.dao.CategoryDao // Keep this for categories
import com.progprof.bargainmargintemplate.data.local.dao.CategorySpendingHistoryDao
import com.progprof.bargainmargintemplate.data.local.entities.CategorySpendingHistoryEntity
import com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity
import com.progprof.bargainmargintemplate.data.local.entities.MonthEntity
import com.progprof.bargainmargintemplate.data.local.entities.WeekEntity

@Database(
    entities = [
        MonthEntity::class,
        WeekEntity::class,
        ExpenseEntity::class,
        CategoryEntity::class,
        CategorySpendingHistoryEntity::class

    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao

    abstract fun categorySpendingHistoryDao(): CategorySpendingHistoryDao
}