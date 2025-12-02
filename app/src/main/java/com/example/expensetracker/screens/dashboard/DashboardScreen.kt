package com.example.expensetracker.screens.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddClick: () -> Unit,
    onViewClick: () -> Unit
) {
    // Access State directly from ViewModel
    val monthlyTotal by viewModel.monthlyTotal
    val weekData by viewModel.weekWiseData

    // Load data once
    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    // Check if there's any data
    val hasData = monthlyTotal > 0.0

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

        if (!hasData) {
            // No data state - centered message with buttons
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No Data Available",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(32.dp))

                // Add Expense Button
                Button(
                    onClick = onAddClick,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Add Expense", fontSize = 16.sp)
                }

                Spacer(Modifier.height(16.dp))

                // View Expenses Button
                OutlinedButton(
                    onClick = onViewClick,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("View Expenses", fontSize = 16.sp)
                }
            }
        } else {
            // Data available - show dashboard
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
}