package com.example.expensetracker.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.data.factory.AddExpenseVMFactory
import com.example.expensetracker.data.factory.DashboardVMFactory
import com.example.expensetracker.data.factory.ViewExpenseVMFactory
import com.example.expensetracker.data.local.AppDatabase
import com.example.expensetracker.data.repository.ExpenseRepository
import com.example.expensetracker.screens.add.AddExpenseScreen
import com.example.expensetracker.screens.add.AddExpenseViewModel
import com.example.expensetracker.screens.dashboard.DashboardScreen
import com.example.expensetracker.screens.dashboard.DashboardViewModel
import com.example.expensetracker.screens.view.ViewExpenseScreen
import com.example.expensetracker.screens.view.ViewExpenseViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    // --- GET CONTEXT PROPERLY ---
    val context = LocalContext.current

    // --- CREATE DB + DAO + REPOSITORY ---
    val db = AppDatabase.getDatabase(context)
    val repo = ExpenseRepository(db.expenseDao())


    // --- CREATE DASHBOARD VIEWMODEL USING FACTORY ---
    val dashboardVM: DashboardViewModel = viewModel(
        factory = DashboardVMFactory(repo)
    )

    val viewExpenseVM: ViewExpenseViewModel = viewModel(
        factory = ViewExpenseVMFactory(repo)
    )

    val addExpenseVM: AddExpenseViewModel = viewModel(
        factory = AddExpenseVMFactory(repo)
    )

    NavHost(
        navController = navController,
        startDestination = "dashboard",
        modifier = modifier
    ) {
        composable("dashboard") {
            DashboardScreen(
                viewModel = dashboardVM,
                onAddClick = { navController.navigate("add_expense") },
                onViewClick = { navController.navigate("view") }
            )
        }

        composable("add_expense") {
            AddExpenseScreen(
                viewModel = addExpenseVM,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable("view") {
            ViewExpenseScreen(
                viewModel = viewExpenseVM
            )
        }
    }
}