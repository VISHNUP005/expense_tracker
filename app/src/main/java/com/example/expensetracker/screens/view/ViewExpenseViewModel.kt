package com.example.expensetracker.screens.view

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.Expense
import com.example.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.launch
import java.util.Calendar

enum class FilterType {
    ALL, WEEKLY, MONTHLY
}

class ViewExpenseViewModel(private val repo: ExpenseRepository) : ViewModel() {

    // Exposed states
    private val _expenses = mutableStateOf<List<Expense>>(emptyList())
    val expenses: State<List<Expense>> = _expenses

    private val _filteredExpenses = mutableStateOf<List<Expense>>(emptyList())
    val filteredExpenses: State<List<Expense>> = _filteredExpenses

    private val _currentFilter = mutableStateOf(FilterType.ALL)
    val currentFilter: State<FilterType> = _currentFilter

    private val _totalAmount = mutableStateOf(0.0)
    val totalAmount: State<Double> = _totalAmount

    init {
        loadAllExpenses()
    }

    private fun loadAllExpenses() {
        viewModelScope.launch {
            repo.getAllExpenses().collect { expenseList ->
                _expenses.value = expenseList
                applyFilter(_currentFilter.value)
            }
        }
    }

    fun setFilter(filterType: FilterType) {
        _currentFilter.value = filterType
        applyFilter(filterType)
    }

    private fun applyFilter(filterType: FilterType) {
        val now = Calendar.getInstance()
        val currentMonth = now.get(Calendar.MONTH) + 1
        val currentYear = now.get(Calendar.YEAR)
        val currentWeek = now.get(Calendar.WEEK_OF_YEAR)

        _filteredExpenses.value = when (filterType) {
            FilterType.ALL -> {
                _expenses.value
            }
            FilterType.MONTHLY -> {
                _expenses.value.filter { expense ->
                    expense.month == currentMonth && expense.year == currentYear
                }
            }
            FilterType.WEEKLY -> {
                _expenses.value.filter { expense ->
                    val expenseCalendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, expense.year)
                        set(Calendar.MONTH, expense.month - 1)
                        set(Calendar.DAY_OF_MONTH, expense.day)
                    }
                    val expenseWeek = expenseCalendar.get(Calendar.WEEK_OF_YEAR)
                    val expenseYear = expense.year

                    expenseWeek == currentWeek && expenseYear == currentYear
                }
            }
        }

        // Calculate total for filtered expenses
        _totalAmount.value = _filteredExpenses.value.sumOf { it.cost }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repo.deleteExpense(expense)
        }
    }

    fun refreshExpenses() {
        loadAllExpenses()
    }
}