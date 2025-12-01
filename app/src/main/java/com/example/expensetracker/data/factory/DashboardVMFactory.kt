package com.example.expensetracker.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.data.repository.ExpenseRepository
import com.example.expensetracker.screens.dashboard.DashboardViewModel

class DashboardVMFactory(
    private val repo: ExpenseRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}