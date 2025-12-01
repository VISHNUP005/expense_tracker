package com.example.expensetracker.screens.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.expensetracker.data.local.Expense

@Composable
fun ViewExpenseScreen(expenses: List<Expense>) {
    LazyColumn {
        items(expenses) { exp ->
            Text("${exp.label} - ₹${exp.cost}", fontSize = 18.sp)
        }
    }
}
