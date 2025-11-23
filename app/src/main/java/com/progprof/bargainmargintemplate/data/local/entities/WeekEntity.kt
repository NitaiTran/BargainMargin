package com.progprof.bargainmargintemplate.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "weeks",
    foreignKeys = [
        ForeignKey(
            entity = MonthEntity::class,
            parentColumns = ["id"],
            childColumns = ["monthId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["monthId"])]
)
data class WeekEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val monthId: Long,
    val weekNumber: Int,
    val weekBudget: Double,
    val weekSpent: Double = 0.0,
    val weeklyGoal: Double = 0.0
)
