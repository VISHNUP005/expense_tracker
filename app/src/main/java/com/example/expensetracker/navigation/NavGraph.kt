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
import com.example.expensetracker.data.factory.DashboardVMFactory
import com.example.expensetracker.data.local.AppDatabase
import com.example.expensetracker.data.repository.ExpenseRepository
import com.example.expensetracker.screens.add.AddExpenseScreen
import com.example.expensetracker.screens.dashboard.DashboardScreen
import com.example.expensetracker.screens.dashboard.DashboardViewModel
import com.example.expensetracker.screens.view.ViewExpenseScreen

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

    NavHost(
        navController = navController,
        startDestination = "dashboard",
        modifier = modifier
    ) {
        composable("dashboard") {
            DashboardScreen(
                viewModel = dashboardVM,
                onAddClick = { navController.navigate("add") },
                onViewClick = { navController.navigate("view") }
            )
        }

//        composable("add") {
//            AddExpenseScreen(
//                viewModel = dashboardVM,
//                onNavigateBack = { navController.popBackStack() }
//            )
//        }
//
//        composable("view") {
//            ViewExpenseScreen(
//                viewModel = dashboardVM,
//                onNavigateBack = { navController.popBackStack() }
//            )
//        }
    }
}