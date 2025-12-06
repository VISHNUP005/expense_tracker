package com.example.expensetracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.navigation.AppNavGraph
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.utils.UpdateManager

class MainActivity : ComponentActivity() {

    private lateinit var updateManager: UpdateManager
    private var showUpdateDialog by mutableStateOf(false)

    // Activity result launcher for update
    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) {
            // Update failed or was cancelled
            // Optionally show a message
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize update manager
        updateManager = UpdateManager(this)

        setContent {
            ExpenseTrackerTheme {
                // Check for updates when app starts
                LaunchedEffect(Unit) {
                    checkForAppUpdate()
                }

                // Update Dialog
                if (showUpdateDialog) {
                    UpdateAvailableDialog(
                        onUpdateClick = {
                            showUpdateDialog = false
                            updateManager.startImmediateUpdate(updateLauncher)
                        },
                        onDismiss = {
                            showUpdateDialog = false
                        }
                    )
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavGraph(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkForAppUpdate()
    }

    private fun checkForAppUpdate() {
        android.util.Log.d("UPDATE_CHECK", "Checking for updates...")

        updateManager.checkForUpdates(
            onUpdateAvailable = {
                android.util.Log.d("UPDATE_CHECK", " Update available!")
                showUpdateDialog = true
            },
            onNoUpdate = {
                android.util.Log.d("UPDATE_CHECK", " No update available")
            },
            onError = { exception ->
                android.util.Log.e("UPDATE_CHECK", "Error: ${exception.message}")
                exception.printStackTrace()
            }
        )
    }

    @Composable
    fun UpdateAvailableDialog(
        onUpdateClick: () -> Unit,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Color(0xFF1E1E2F),
            title = {
                Column {
                    Text(
                        text = "🎉 Update Available!",
                        color = colorResource(R.color.soft_white),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "A new version of Expense Tracker is available with bug fixes and improvements.",
                        color = colorResource(R.color.soft_blue_dark),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Update now to get the latest features!",
                        color = colorResource(R.color.soft_white),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onUpdateClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.soft_blue),
                        contentColor = colorResource(R.color.black)
                    )
                ) {
                    Text(
                        text = "Update Now",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colorResource(R.color.soft_blue_dark)
                    )
                ) {
                    Text("Later")
                }
            }
        )
    }
}