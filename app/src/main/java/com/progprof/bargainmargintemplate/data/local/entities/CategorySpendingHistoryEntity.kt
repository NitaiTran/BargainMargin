package com.progprof.bargainmargintemplate.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_spending_history",
    indices = [Index(value = ["categoryId", "year", "month"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CategorySpendingHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryId: Int,
    val year: Int,
    val month: Int,
    val amountSpent: Double,
    val totalBudgetAtSnapshot: Double
)
