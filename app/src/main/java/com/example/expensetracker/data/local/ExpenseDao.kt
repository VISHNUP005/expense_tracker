package com.example.expensetracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expense WHERE day = :day AND month = :month AND year = :year")
    suspend fun getExpensesByDay(day: String, month: Int, year: Int): List<Expense>


    @Query("SELECT * FROM expense WHERE month = :month AND year = :year")
    suspend fun getMonthlyExpenses(month: Int, year: Int): List<Expense>

    @Query("SELECT SUM(cost) FROM expense WHERE month = :month AND year = :year")
    suspend fun getMonthlyTotal(month: Int, year: Int): Double?

    @Query("SELECT * FROM expense ORDER BY year DESC, month DESC, day DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expense WHERE month = :month AND year = :year ORDER BY day DESC")
    suspend fun getExpensesByMonth(month: Int, year: Int): List<Expense>


    @Query("SELECT * FROM expense WHERE day = :day AND month = :month AND year = :year ORDER BY id DESC")
    suspend fun getDailyExpenses(day: Int, month: Int, year: Int): List<Expense>

    @Query("SELECT SUM(cost) FROM expense WHERE day = :day AND month = :month AND year = :year")
    suspend fun getDailyTotal(day: Int, month: Int, year: Int): Double?
}
