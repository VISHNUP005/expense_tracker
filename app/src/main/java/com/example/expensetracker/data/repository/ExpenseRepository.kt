package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.Expense
import com.example.expensetracker.data.local.ExpenseDao

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
}