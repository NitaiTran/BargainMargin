package com.progprof.bargainmargintemplate.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryName: String,
    val totalBudget: Double,
    val budgetRemaining: Double
)
