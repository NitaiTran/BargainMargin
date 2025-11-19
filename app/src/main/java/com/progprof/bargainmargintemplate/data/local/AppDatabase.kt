package com.progprof.bargainmargintemplate.data.local
import com.progprof.bargainmargintemplate.data.local.entities.BudgetEntity
import com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import com.progprof.bargainmargintemplate.data.local.dao.BudgetDao
import com.progprof.bargainmargintemplate.data.local.dao.CategoryDao
import com.progprof.bargainmargintemplate.data.local.dao.ExpenseDao
import com.progprof.bargainmargintemplate.data.local.dao.MonthDao
import com.progprof.bargainmargintemplate.data.local.dao.WeekDao

@Database(
    entities = [ExpenseEntity::class, BudgetEntity::class, CategoryEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun monthDao(): MonthDao
    abstract fun weekDao(): WeekDao
}
