package com.example.expensetracker.screens.add

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.Expense
import com.example.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddExpenseViewModel(private val repo: ExpenseRepository) : ViewModel() {

    private val _label = MutableStateFlow("")
    val label: StateFlow<String> = _label

    private val _cost = MutableStateFlow("")
    val cost: StateFlow<String> = _cost

    private val _category = MutableStateFlow<String?>(null)
    val category: StateFlow<String?> = _category

    private val _todayTotal = MutableStateFlow(0.0)
    val todayTotal: StateFlow<Double> = _todayTotal

    init {
        loadTodayTotal()
    }

    fun updateLabel(value: String) {
        _label.value = value
    }

    fun updateCost(value: String) {
        _cost.value = value
        updatePreviewTotal()
    }

    fun updateCategory(value: String) {
        _category.value = value
    }

    /** Updates the preview total with current input **/
    private fun updatePreviewTotal() {
        viewModelScope.launch {
            val now = Calendar.getInstance()
            val day = now.get(Calendar.DAY_OF_MONTH).toString()
            val month = now.get(Calendar.MONTH) + 1
            val year = now.get(Calendar.YEAR)

            val dailyExpenses = repo.getExpensesByDay(day, month, year)
            val existingTotal = dailyExpenses.sumOf { it.cost }
            val currentInput = _cost.value.toDoubleOrNull() ?: 0.0

            _todayTotal.value = existingTotal + currentInput
        }
    }

    /** Returns today's total without modifying DB (for live update) **/
    fun getTodayTotal(): Double = _todayTotal.value

    /** Add expense to DB **/
    fun addExpense() {
        viewModelScope.launch {
            val costValue = _cost.value.toDoubleOrNull() ?: 0.0
            if (_label.value.isNotBlank() && costValue > 0 && _category.value != null) {
                val now = Calendar.getInstance()
                val expense = Expense(
                    id = 0,
                    label = _label.value.trim(),
                    cost = costValue,
                    day = now.get(Calendar.DAY_OF_MONTH),
                    month = now.get(Calendar.MONTH) + 1,
                    year = now.get(Calendar.YEAR),
                    category = category.value
                )

                repo.addExpense(expense)
                clearFields()
                loadTodayTotal()
            }
        }
    }

    /** Load total for current day from DB - Call this when screen becomes visible **/
    fun loadTodayTotal() {
        viewModelScope.launch {
            val now = Calendar.getInstance()
            val day = now.get(Calendar.DAY_OF_MONTH).toString()
            val month = now.get(Calendar.MONTH) + 1
            val year = now.get(Calendar.YEAR)

            val dailyExpenses = repo.getExpensesByDay(day, month, year)
            _todayTotal.value = dailyExpenses.sumOf { it.cost }
        }
    }

    /** Call this method when the screen is resumed/visible **/
    fun refreshData() {
        loadTodayTotal()
    }

    fun clearFields() {
        _label.value = ""
        _cost.value = ""
        _category.value = null
    }
}