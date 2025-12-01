package com.example.expensetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expense WHERE month = :month AND year = :year")
    suspend fun getMonthlyExpenses(month: Int, year: Int): List<Expense>

    @Query("SELECT SUM(cost) FROM expense WHERE month = :month AND year = :year")
    suspend fun getMonthlyTotal(month: Int, year: Int): Double?
}