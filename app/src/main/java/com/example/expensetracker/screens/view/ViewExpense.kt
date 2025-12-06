package com.example.expensetracker.screens.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R
import com.example.expensetracker.data.local.Expense

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewExpenseScreen(
    viewModel: ViewExpenseViewModel,
    onBack: () -> Unit = {}
) {
    val filteredExpenses = viewModel.filteredExpenses.value
    val currentFilter = viewModel.currentFilter.value
    val totalAmount = viewModel.totalAmount.value

    var showDeleteDialog by remember { mutableStateOf(false) }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    LaunchedEffect(Unit) {
        viewModel.refreshExpenses()
    }

    Scaffold(
        containerColor = colorResource(R.color.black)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "View Expenses",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.soft_white)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Total Amount Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (currentFilter) {
                            FilterType.ALL -> "Total Expenses"
                            FilterType.WEEKLY -> "This Week"
                            FilterType.MONTHLY -> "This Month"
                        },
                        fontSize = 16.sp,
                        color = colorResource(R.color.soft_blue_dark)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "₹${String.format("%.2f", totalAmount)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.soft_white)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Filter Chips
            Text(
                text = "Filter By",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.soft_blue_dark),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChipCustom(
                    label = "All",
                    selected = currentFilter == FilterType.ALL,
                    onClick = { viewModel.setFilter(FilterType.ALL) },
                    modifier = Modifier.weight(1f)
                )
                FilterChipCustom(
                    label = "Weekly",
                    selected = currentFilter == FilterType.WEEKLY,
                    onClick = { viewModel.setFilter(FilterType.WEEKLY) },
                    modifier = Modifier.weight(1f)
                )
                FilterChipCustom(
                    label = "Monthly",
                    selected = currentFilter == FilterType.MONTHLY,
                    onClick = { viewModel.setFilter(FilterType.MONTHLY) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Expenses List
            if (filteredExpenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No expenses found",
                        fontSize = 18.sp,
                        color = colorResource(R.color.soft_blue_dark)
                    )
                }
            } else {
                Text(
                    text = "${filteredExpenses.size} Transaction${if (filteredExpenses.size != 1) "s" else ""}",
                    fontSize = 14.sp,
                    color = colorResource(R.color.soft_blue_dark),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredExpenses, key = { it.id }) { expense ->
                        ExpenseItem(
                            expense = expense,
                            onDelete = {
                                expenseToDelete = expense
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && expenseToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color(0xFF1E1E2F),
            title = {
                Text(
                    text = "Delete Expense?",
                    color = colorResource(R.color.soft_white)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete '${expenseToDelete?.label}'?",
                    color = colorResource(R.color.soft_blue_dark)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        expenseToDelete?.let { viewModel.deleteExpense(it) }
                        showDeleteDialog = false
                        expenseToDelete = null
                    }
                ) {
                    Text("Delete", color = colorResource(R.color.soft_blue))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        expenseToDelete = null
                    }
                ) {
                    Text("Cancel", color = colorResource(R.color.soft_blue_dark))
                }
            }
        )
    }
}

@Composable
fun FilterChipCustom(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (selected) colorResource(R.color.soft_blue) else Color(0xFF2A2A3B),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) colorResource(R.color.black) else colorResource(R.color.soft_white)
            )
        }
    }
}

@Composable
fun ExpenseItem(
    expense: Expense,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2F)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.label,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.soft_white)
                )
                Spacer(Modifier.height(4.dp))

                // Category
                if (!expense.category.isNullOrEmpty()) {
                    Surface(
                        color = Color(0xFF2A2A3B),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = expense.category,
                            fontSize = 11.sp,
                            color = colorResource(R.color.soft_blue),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = "${expense.day}/${expense.month}/${expense.year}",
                    fontSize = 13.sp,
                    color = colorResource(R.color.soft_blue_dark)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "₹${String.format("%.2f", expense.cost)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.soft_blue)
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete expense",
                        tint = Color(0xFFFF6B6B)
                    )
                }
            }
        }
    }
}