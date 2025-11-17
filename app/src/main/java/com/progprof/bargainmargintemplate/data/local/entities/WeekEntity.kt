package com.progprof.bargainmargintemplate.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthlyData")
data class WeekEntity(
    @PrimaryKey val id: Int = 0,
    val myBudgetEntity: BudgetEntity,
    val myCategoryEntity: CategoryEntity,
    val myExpenseEntity: ExpenseEntity

)
