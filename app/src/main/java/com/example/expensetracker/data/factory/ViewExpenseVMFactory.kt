package com.example.expensetracker.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.data.repository.ExpenseRepository
import com.example.expensetracker.screens.view.ViewExpenseViewModel

class ViewExpenseVMFactory(private val repo: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewExpenseViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}