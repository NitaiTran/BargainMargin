package com.progprof.bargainmargintemplate.data.repository

import com.progprof.bargainmargintemplate.data.local.AppDatabase
import com.progprof.bargainmargintemplate.data.local.entities.BudgetEntity
import com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity
import kotlinx.coroutines.flow.firstOrNull

class BudgetRepository(private val db: AppDatabase) {

    val allExpenses = db.expenseDao().getAllExpenses()
    val allCategories = db.categoryDao().getAllCategories()
    val budget = db.budgetDao().getBudget()

    suspend fun initializeIfEmpty() {
        val current = db.budgetDao().getBudget().firstOrNull()
        if (current == null) {
            db.budgetDao().upsertBudget(BudgetEntity())
        }
    }

    suspend fun updateBudget(newBudget: BudgetEntity) {
        db.budgetDao().upsertBudget(newBudget)
    }

    suspend fun insertExpense(expenseEntity: ExpenseEntity) {
        db.expenseDao().insertExpense(expenseEntity)
    }

    suspend fun deleteExpense(expenseEntity: ExpenseEntity) {
        db.expenseDao().deleteExpense(expenseEntity)
    }

    suspend fun insertCategory(categoryEntity: CategoryEntity) {
        db.categoryDao().insertCategory(categoryEntity)
    }

    suspend fun deleteCategory(categoryEntity: CategoryEntity) {
        db.categoryDao().deleteCategory(categoryEntity)
    }

    suspend fun updateCategory(categoryEntity: CategoryEntity) {
        db.categoryDao().updateCategory(categoryEntity)
    }
}
