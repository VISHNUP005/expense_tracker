package com.example.expensetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.MutableStateFlow

@Entity(tableName = "expense")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val label: String,
    val day: Int,
    val cost: Double,
    val month: Int,
    val year: Int,
    val category: String?
)
