package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.Expense
import com.example.expensetracker.data.local.ExpenseDao
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {

    suspend fun addExpense(expense: Expense) {
        dao.insertExpense(expense)
    }

    suspend fun getMonthlyExpenses(month: Int, year: Int): List<Expense> {
        return dao.getMonthlyExpenses(month, year)
    }

    suspend fun getMonthlyTotal(month: Int, year: Int): Double {
        return dao.getMonthlyTotal(month, year) ?: 0.0
    }

    fun getAllExpenses(): Flow<List<Expense>> {
        return dao.getAllExpenses()
    }


    suspend fun getExpensesByMonth(month: Int, year: Int): List<Expense> {
        return dao.getExpensesByMonth(month, year)

    }

    suspend fun getDailyTotal(day: Int, month: Int, year: Int): Double {
        return dao.getDailyTotal(day, month, year) ?: 0.0
    }


    suspend fun getExpensesByDay(day: String, month: Int, year: Int): List<Expense> {
        return dao.getExpensesByDay(day, month, year)
    }

    suspend fun deleteExpense(expense: Expense) {
        dao.deleteExpense(expense)
    }
}