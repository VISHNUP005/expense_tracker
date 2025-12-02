package com.example.expensetracker.screens.add

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    var label by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Current Month Expenses",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "₹${String.format("%.2f", viewModel.subtotal.value)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Expense Label Input
            OutlinedTextField(
                value = label,
                onValueChange = {
                    label = it
                    viewModel.updateLabel(it)
                    showError = false
                },
                label = { Text("Expense Label") },
                placeholder = { Text("e.g., Groceries, Transport") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showError && label.isEmpty(),
                supportingText = {
                    if (showError && label.isEmpty()) {
                        Text("Label cannot be empty", color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            // Cost Input
            OutlinedTextField(
                value = cost,
                onValueChange = {
                    // Only allow numbers and decimal point
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        cost = it
                        viewModel.updateCost(it)
                        showError = false
                    }
                },
                label = { Text("Amount") },
                placeholder = { Text("0.00") },
                leadingIcon = { Text("₹", fontSize = 18.sp) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                isError = showError && (cost.isEmpty() || cost.toDoubleOrNull() == null || cost.toDoubleOrNull() == 0.0),
                supportingText = {
                    if (showError && (cost.isEmpty() || cost.toDoubleOrNull() == null || cost.toDoubleOrNull() == 0.0)) {
                        Text("Please enter a valid amount", color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(Modifier.height(32.dp))

            // Error Message
            if (showError && errorMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            // Save Button
            Button(
                onClick = {
                    when {
                        label.trim().isEmpty() -> {
                            showError = true
                            errorMessage = "Please enter an expense label"
                        }
                        cost.isEmpty() || cost.toDoubleOrNull() == null || cost.toDoubleOrNull() == 0.0 -> {
                            showError = true
                            errorMessage = "Please enter a valid amount"
                        }
                        else -> {
                            viewModel.addExpense()
                            onSave()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Save Expense", fontSize = 18.sp)
            }

            Spacer(Modifier.height(16.dp))

            // Cancel Button
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Cancel", fontSize = 18.sp)
            }
        }
    }
}