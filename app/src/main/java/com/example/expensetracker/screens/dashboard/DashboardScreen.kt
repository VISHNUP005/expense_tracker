package com.example.expensetracker.screens.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddClick: () -> Unit,
    onViewClick: () -> Unit
) {
    // Access State directly from ViewModel (no need to collect)
    val monthlyTotal by viewModel.monthlyTotal
    val weekData by viewModel.weekWiseData

    // Load data once
    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    Scaffold(
        floatingActionButton = {
            Column {
                // Add Expense FAB
                FloatingActionButton(
                    onClick = onAddClick,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }

                // View Expense FAB
                FloatingActionButton(
                    onClick = onViewClick
                ) {
                    Icon(Icons.Default.Info, contentDescription = "View Expenses")
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Total Spent: ₹$monthlyTotal",
                fontSize = 22.sp
            )

            Spacer(Modifier.height(20.dp))

            Text("Week-wise Summary", fontSize = 18.sp)

            Spacer(Modifier.height(10.dp))

            // Display week-wise data
            weekData.forEach { (week, amount) ->
                if (amount > 0.0) {
                    Text(
                        text = "$week: ₹$amount",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}