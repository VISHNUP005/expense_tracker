package com.example.expensetracker.screens.add
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    val itemName by viewModel.label.collectAsState()
    val amount by viewModel.cost.collectAsState()
    val todayTotal by viewModel.todayTotal.collectAsState()
    val selectedCategory by viewModel.category.collectAsState()

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showCategorySheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    data class CategoryItem(val name: String, val iconRes: Int)

    val categories = listOf(
        CategoryItem("Food", R.drawable.food),
        CategoryItem("Shopping", R.drawable.shopping),
        CategoryItem("Travelling", R.drawable.travel),
        CategoryItem("Entertainment", R.drawable.cinema),
        CategoryItem("Medical", R.drawable.medicine),
        CategoryItem("Personal Care", R.drawable.personal),
        CategoryItem("Education", R.drawable.education),
        CategoryItem("Bills & Utilities", R.drawable.bill),
        CategoryItem("Investments", R.drawable.investment),
        CategoryItem("Rent", R.drawable.rent),
        CategoryItem("Gifts", R.drawable.giftbox),
        CategoryItem("Donation", R.drawable.donation)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.black))
            .padding(18.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Add Transaction",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.soft_white)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Today's total card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2F))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Today's Expenses",
                    fontSize = 16.sp,
                    color = colorResource(R.color.soft_blue_dark)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "₹${String.format("%.2f", todayTotal)}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.soft_white)
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        // Item name field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Item",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.soft_blue_dark)
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                value = itemName,
                onValueChange = {
                    viewModel.updateLabel(it)
                    showError = false
                },
                placeholder = { Text("Enter item", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A2A3B),
                    unfocusedContainerColor = Color(0xFF2A2A3B),
                    focusedTextColor = colorResource(R.color.soft_white),
                    unfocusedTextColor = colorResource(R.color.soft_white),
                    cursorColor = colorResource(R.color.soft_white),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Note,
                        contentDescription = "Item Icon",
                        tint = colorResource(R.color.soft_blue)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(16.dp))

        // Amount field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Amount",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.soft_blue_dark)
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                value = amount,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        viewModel.updateCost(it)
                        showError = false
                    }
                },
                placeholder = { Text("0.00", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A2A3B),
                    unfocusedContainerColor = Color(0xFF2A2A3B),
                    focusedTextColor = colorResource(R.color.soft_white),
                    unfocusedTextColor = colorResource(R.color.soft_white),
                    cursorColor = colorResource(R.color.soft_white),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Amount Icon",
                        tint = colorResource(R.color.soft_blue)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(16.dp))

        // Category selector
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Category",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.soft_blue_dark)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2A2A3B), RoundedCornerShape(8.dp))
                    .clickable {
                        showCategorySheet = true
                        coroutineScope.launch { sheetState.show() }
                    }
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = "Category Icon",
                        tint = colorResource(R.color.soft_blue)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = selectedCategory ?: "Select Category",
                        color = if (selectedCategory == null) Color.Gray else colorResource(R.color.soft_white),
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // Error message
        if (showError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Bottom buttons row
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancel button
            OutlinedButton(
                onClick = onBack,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorResource(R.color.soft_blue)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp
                )
            }

            // Save button
            Button(
                onClick = {
                    when {
                        itemName.trim().isEmpty() -> {
                            showError = true
                            errorMessage = "Please enter an item"
                        }
                        amount.isEmpty() || amount.toDoubleOrNull() == null || amount.toDoubleOrNull() == 0.0 -> {
                            showError = true
                            errorMessage = "Please enter a valid amount"
                        }
                        selectedCategory == null -> {
                            showError = true
                            errorMessage = "Please select a category"
                        }
                        else -> {
                            viewModel.addExpense()
                            onSave()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.soft_blue),
                    contentColor = colorResource(R.color.black)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Save",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }


    if (showCategorySheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showCategorySheet = false
                coroutineScope.launch { sheetState.hide() }
            },
            sheetState = sheetState,
            containerColor = Color(0xFF1E1E2F)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select Category",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.soft_white),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { category ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .background(
                                    if (selectedCategory == category.name)
                                        colorResource(R.color.soft_blue)
                                    else
                                        Color(0xFF2A2A3B),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    viewModel.updateCategory(category.name)
                                    showCategorySheet = false
                                    coroutineScope.launch { sheetState.hide() }
                                }
                                .padding(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = category.iconRes),
                                    contentDescription = category.name,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(bottom = 8.dp)
                                )
                                Text(
                                    text = category.name,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedCategory == category.name) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedCategory == category.name)
                                        colorResource(R.color.black)
                                    else
                                        colorResource(R.color.soft_white),
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}