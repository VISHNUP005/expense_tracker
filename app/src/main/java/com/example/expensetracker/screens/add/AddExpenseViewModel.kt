package com.example.expensetracker.screens.add

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.Expense
import com.example.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class AddExpenseViewModel(private val repo: ExpenseRepository) : ViewModel() {

    var label: String = ""
    var cost: Double = 0.0
    var subtotal: Double = 0.0

    fun updateLabel(value: String) {
        label = value
    }

    fun updateCost(value: String) {
        cost = value.toDoubleOrNull() ?: 0.0
        subtotal = cost
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addExpense() {
        viewModelScope.launch {
            val now = Calendar.getInstance()

            val expense = Expense(
                label = label,
                cost = cost,
                month = now.get(Calendar.MONTH) + 1,
                year = now.get(Calendar.YEAR),
                id = TODO(),
                day = TODO()
            )

            repo.addExpense(expense)
        }
    }
}