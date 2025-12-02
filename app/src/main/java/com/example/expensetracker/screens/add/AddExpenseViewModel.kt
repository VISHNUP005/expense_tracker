package com.example.expensetracker.screens.add

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.Expense
import com.example.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class AddExpenseViewModel(private val repo: ExpenseRepository) : ViewModel() {

    // Exposed states
    private val _label = mutableStateOf("")
    val label: State<String> = _label

    private val _cost = mutableStateOf("")
    val cost: State<String> = _cost

    private val _subtotal = mutableStateOf(0.0)
    val subtotal: State<Double> = _subtotal

    init {
        loadCurrentMonthTotal()
    }

    fun updateLabel(value: String) {
        _label.value = value
    }

    fun updateCost(value: String) {
        _cost.value = value
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addExpense() {
        viewModelScope.launch {
            val costValue = _cost.value.toDoubleOrNull() ?: 0.0
            if (_label.value.isNotBlank() && costValue > 0) {
                val now = Calendar.getInstance()

                val expense = Expense(
                    id = 0, // Room auto-generates this
                    label = _label.value.trim(),
                    cost = costValue,
                    day = now.get(Calendar.DAY_OF_MONTH).toString(),
                    month = now.get(Calendar.MONTH) + 1, // Calendar.MONTH is 0-based
                    year = now.get(Calendar.YEAR)
                )

                repo.addExpense(expense)

                // Clear fields after saving
                _label.value = ""
                _cost.value = ""

                // Reload subtotal
                loadCurrentMonthTotal()
            }
        }
    }

    private fun loadCurrentMonthTotal() {
        viewModelScope.launch {
            val now = Calendar.getInstance()
            val month = now.get(Calendar.MONTH) + 1
            val year = now.get(Calendar.YEAR)

            val total = repo.getMonthlyTotal(month, year)
            _subtotal.value = total
        }
    }

    fun clearFields() {
        _label.value = ""
        _cost.value = ""
    }
}