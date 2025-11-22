package com.progprof.bargainmargintemplate.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "months",
    indices = [Index(value = ["year", "month"], unique = true)]
)
data class MonthEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val year: Int,
    val month: Int,
    val totalBudget: Double,
    val totalSpent: Double = 0.0
)
