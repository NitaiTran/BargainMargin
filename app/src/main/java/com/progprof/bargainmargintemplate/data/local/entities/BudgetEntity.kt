package com.progprof.bargainmargintemplate.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey val id: Int = 0,
    val totalBudget: Double = 0.0,
    val totalRemainingBudget: Double = 0.0,
    val monthlyRemainingBudget: Double = 0.0,
    val week1TotalBudget: Double = 0.0,
    val week1RemainingBudget: Double = 0.0,
    val week2TotalBudget: Double = 0.0,
    val week2RemainingBudget: Double = 0.0,
    val week3TotalBudget: Double = 0.0,
    val week3RemainingBudget: Double = 0.0,
    val week4TotalBudget: Double = 0.0,
    val week4RemainingBudget: Double = 0.0,
    val myCurrentWeek: Int = 1
)
