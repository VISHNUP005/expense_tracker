package com.example.expensetracker.screens.add

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpenseScreen(viewModel: AddExpenseViewModel, onSave: () -> Unit) {
    var label by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = label,
            onValueChange = {
                label = it
                viewModel.updateLabel(it)
            },
            label = { Text("Expense Label") }
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = cost,
            onValueChange = {
                cost = it
                viewModel.updateCost(it)
            },
            label = { Text("Cost") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(16.dp))

        Text("Subtotal: ₹${viewModel.subtotal}", fontSize = 20.sp)

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            viewModel.addExpense()
            onSave()
        }) {
            Text("Save")
        }
    }
}
