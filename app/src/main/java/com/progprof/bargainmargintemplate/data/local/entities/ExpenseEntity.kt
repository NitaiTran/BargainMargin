package com.progprof.bargainmargintemplate.data.local.entities


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    indices = [Index(value = ["weekId"])],
    foreignKeys = [
        ForeignKey(
            entity = WeekEntity::class,
            parentColumns = ["id"],
            childColumns = ["weekId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val weekId: Long,
    val amountOfExpense: Double,
    val descriptionOfExpense: String,
    val categoryOfExpense: String
)