package com.example.expensetracker.screens.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.Expense
import com.example.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardViewModel(
    private val repo: ExpenseRepository
) : ViewModel() {

    // Exposed states for Compose
    private val _monthlyTotal = mutableStateOf(0.0)
    val monthlyTotal: State<Double> = _monthlyTotal

    private val _monthlyExpenses = mutableStateOf<List<Expense>>(emptyList())
    val monthlyExpenses: State<List<Expense>> = _monthlyExpenses

    private val _weekWiseData = mutableStateOf<Map<String, Double>>(emptyMap())
    val weekWiseData: State<Map<String, Double>> = _weekWiseData

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDashboard() {
        viewModelScope.launch {
            val now = Calendar.getInstance()
            val month = now.get(Calendar.MONTH) + 1   // 0-based → +1
            val year = now.get(Calendar.YEAR)

            val expenses = repo.getMonthlyExpenses(month, year)
            val total = repo.getMonthlyTotal(month, year)

            _monthlyExpenses.value = expenses
            _monthlyTotal.value = total

            _weekWiseData.value = calculateWeekWise(expenses)
        }
    }

    /** Correct week-wise grouping by extracting day from day field */
    private fun calculateWeekWise(expenses: List<Expense>): Map<String, Double> {

        val result = mutableMapOf(
            "Week 1" to 0.0,
            "Week 2" to 0.0,
            "Week 3" to 0.0,
            "Week 4" to 0.0,
            "Week 5" to 0.0
        )

        for (expense in expenses) {
            try {
                // Convert day string to int
                val day = expense.day

                when (day) {
                    in 1..7 -> result["Week 1"] = result["Week 1"]!! + expense.cost
                    in 8..14 -> result["Week 2"] = result["Week 2"]!! + expense.cost
                    in 15..21 -> result["Week 3"] = result["Week 3"]!! + expense.cost
                    in 22..28 -> result["Week 4"] = result["Week 4"]!! + expense.cost
                    else -> result["Week 5"] = result["Week 5"]!! + expense.cost
                }
            } catch (e: Exception) {
                // Skip if conversion fails
                continue
            }
        }

        return result
    }
}