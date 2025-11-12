package com.progprof.bargainmargintemplate.data.local.entities


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amountOfExpense: Double,
    val descriptionOfExpense: String,
    val categoryOfExpense: String,
    val weekOfExpense: Int
)
