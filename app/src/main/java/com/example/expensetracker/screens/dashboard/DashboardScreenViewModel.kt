
// DashboardViewModel.kt
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

data class StatsData(
    val payPerDay: Double = 0.0,
    val payPerTransaction: Double = 0.0,
    val totalTransactions: Int = 0
)

class DashboardViewModel(
    private val repo: ExpenseRepository
) : ViewModel() {

    private val _monthlyTotal = mutableStateOf(0.0)
    val monthlyTotal: State<Double> = _monthlyTotal

    private val _monthlyExpenses = mutableStateOf<List<Expense>>(emptyList())
    val monthlyExpenses: State<List<Expense>> = _monthlyExpenses

    private val _weekWiseData = mutableStateOf<Map<String, Double>>(emptyMap())
    val weekWiseData: State<Map<String, Double>> = _weekWiseData

    private val _categoryWiseData = mutableStateOf<Map<String, Double>>(emptyMap())
    val categoryWiseData: State<Map<String, Double>> = _categoryWiseData

    private val _statsData = mutableStateOf(StatsData())
    val statsData: State<StatsData> = _statsData

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDashboard() {
        viewModelScope.launch {
            val now = Calendar.getInstance()
            val month = now.get(Calendar.MONTH) + 1
            val year = now.get(Calendar.YEAR)

            val expenses = repo.getMonthlyExpenses(month, year)
            val total = repo.getMonthlyTotal(month, year)

            _monthlyExpenses.value = expenses
            _monthlyTotal.value = total

            _weekWiseData.value = calculateWeekWise(expenses)
            _categoryWiseData.value = calculateCategoryWise(expenses)
            _statsData.value = calculateStats(expenses, now.get(Calendar.DAY_OF_MONTH))
        }
    }

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
                val day = expense.day
                when (day) {
                    in 1..7 -> result["Week 1"] = result["Week 1"]!! + expense.cost
                    in 8..14 -> result["Week 2"] = result["Week 2"]!! + expense.cost
                    in 15..21 -> result["Week 3"] = result["Week 3"]!! + expense.cost
                    in 22..28 -> result["Week 4"] = result["Week 4"]!! + expense.cost
                    else -> result["Week 5"] = result["Week 5"]!! + expense.cost
                }
            } catch (e: Exception) {
                continue
            }
        }

        return result
    }

    private fun calculateCategoryWise(expenses: List<Expense>): Map<String, Double> {
        val categoryMap = mutableMapOf<String, Double>()

        for (expense in expenses) {
            val category = expense.category ?: "Other"
            categoryMap[category] = (categoryMap[category] ?: 0.0) + expense.cost
        }

        return categoryMap
    }

    private fun calculateStats(expenses: List<Expense>, currentDay: Int): StatsData {
        val totalTransactions = expenses.size
        val totalAmount = expenses.sumOf { it.cost }

        val payPerDay = if (currentDay > 0) totalAmount / currentDay else 0.0
        val payPerTransaction = if (totalTransactions > 0) totalAmount / totalTransactions else 0.0

        return StatsData(
            payPerDay = payPerDay,
            payPerTransaction = payPerTransaction,
            totalTransactions = totalTransactions
        )
    }
}