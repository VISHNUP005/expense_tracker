// DashboardScreen.kt
package com.example.expensetracker.screens.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddClick: () -> Unit,
    onViewClick: () -> Unit
) {
    val monthlyTotal by viewModel.monthlyTotal
    val weekData by viewModel.weekWiseData
    val categoryData by viewModel.categoryWiseData
    val statsData by viewModel.statsData

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    val hasData = monthlyTotal > 0.0
    var fabExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colorResource(R.color.black),
        floatingActionButton = {
            Box {
                AnimatedVisibility(
                    visible = fabExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-60).dp, y = (-60).dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = Color(0xFF2A2A3B),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "View Expenses",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = colorResource(R.color.soft_white),
                                fontSize = 14.sp
                            )
                        }
                        SmallFloatingActionButton(
                            onClick = {
                                onViewClick()
                                fabExpanded = false
                            },
                            containerColor = Color(0xFF2A2A3B),
                            contentColor = colorResource(R.color.soft_blue)
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "View Expenses",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Add Expense Mini FAB (40 degrees)
                AnimatedVisibility(
                    visible = fabExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-80).dp, y = (5).dp)
                )
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = Color(0xFF2A2A3B),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Add Expense",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = colorResource(R.color.soft_white),
                                fontSize = 14.sp
                            )
                        }
                        SmallFloatingActionButton(
                            onClick = {
                                onAddClick()
                                fabExpanded = false
                            },
                            containerColor = Color(0xFF2A2A3B),
                            contentColor = colorResource(R.color.soft_blue)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add Expense",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Main FAB
                FloatingActionButton(
                    onClick = { fabExpanded = !fabExpanded },
                    containerColor = colorResource(R.color.soft_blue),
                    contentColor = colorResource(R.color.black),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    val rotation by animateFloatAsState(
                        targetValue = if (fabExpanded) 45f else 0f,
                        label = "FAB rotation"
                    )
                    Icon(
                        imageVector = if (fabExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = if (fabExpanded) "Close menu" else "Open menu",
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }
        }
    ) { innerPadding ->

        if (!hasData) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = "No Data Available",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.soft_white)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.statisctics),
                        contentDescription = "Dashbaord",
                        modifier = Modifier
                            .size(35.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "My Dashboard",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.soft_white)
                    )

                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Total Spent: ₹${String.format("%.2f", monthlyTotal)}",
                    fontSize = 20.sp,
                    color = colorResource(R.color.soft_blue)
                )

                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2F)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Category-wise Spending",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.soft_white)
                        )

                        Spacer(Modifier.height(16.dp))

                        if (categoryData.isNotEmpty()) {
                            PieChart(
                                data = categoryData,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                            )

                            Spacer(Modifier.height(16.dp))

                            CategoryLegend(data = categoryData)
                        } else {
                            Text(
                                text = "No category data",
                                color = colorResource(R.color.soft_blue_dark),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Statistics",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.soft_white)
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Pay/Day",
                        value = "₹${String.format("%.2f", statsData.payPerDay)}",
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "Pay/Transaction",
                        value = "₹${String.format("%.2f", statsData.payPerTransaction)}",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                StatCard(
                    title = "Total Transactions",
                    value = statsData.totalTransactions.toString(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2F)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Week-wise Spending",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.soft_white)
                        )

                        Spacer(Modifier.height(16.dp))

                        val weekDataFiltered = weekData.filter { it.value > 0 }

                        if (weekDataFiltered.isNotEmpty()) {
                            PieChart(
                                data = weekDataFiltered,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                            )

                            Spacer(Modifier.height(16.dp))

                            CategoryLegend(data = weekDataFiltered)
                        } else {
                            Text(
                                text = "No week data",
                                color = colorResource(R.color.soft_blue_dark),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2F)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = colorResource(R.color.soft_blue_dark)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.soft_white)
            )
        }
    }
}

@Composable
fun PieChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color(0xFF6C63FF),
        Color(0xFF4ECDC4),
        Color(0xFFFF6B6B),
        Color(0xFFFFA500),
        Color(0xFF95E1D3),
        Color(0xFFF38181),
        Color(0xFFAA96DA),
        Color(0xFFFCBF49),
        Color(0xFF06FFA5),
        Color(0xFF118AB2),
        Color(0xFFEF476F),
        Color(0xFF06D6A0)
    )

    val total = data.values.sum()
    val proportions = data.values.map { (it / total * 360f).toFloat() }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = size.minDimension
            val radius = canvasSize / 2.5f
            val center = Offset(size.width / 2f, size.height / 2f)

            var startAngle = -90f

            proportions.forEachIndexed { index, angle ->
                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = angle,
                    useCenter = true,
                    topLeft = Offset(
                        center.x - radius,
                        center.y - radius
                    ),
                    size = Size(radius * 2, radius * 2)
                )

                drawArc(
                    color = Color(0xFF1E1E2F),
                    startAngle = startAngle,
                    sweepAngle = angle,
                    useCenter = true,
                    topLeft = Offset(
                        center.x - radius,
                        center.y - radius
                    ),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 3f)
                )

                startAngle += angle
            }

            drawCircle(
                color = Color(0xFF1E1E2F),
                radius = radius * 0.5f,
                center = center
            )
        }
    }
}

@Composable
fun CategoryLegend(
    data: Map<String, Double>
) {
    val colors = listOf(
        Color(0xFF6C63FF), Color(0xFF4ECDC4), Color(0xFFFF6B6B), Color(0xFFFFA500),
        Color(0xFF95E1D3), Color(0xFFF38181), Color(0xFFAA96DA), Color(0xFFFCBF49),
        Color(0xFF06FFA5), Color(0xFF118AB2), Color(0xFFEF476F), Color(0xFF06D6A0)
    )

    val total = data.values.sum()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        data.entries.forEachIndexed { index, entry ->
            val percentage = (entry.value / total * 100)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                colors[index % colors.size],
                                RoundedCornerShape(4.dp)
                            )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = entry.key,
                        fontSize = 14.sp,
                        color = colorResource(R.color.soft_white)
                    )
                }

                Text(
                    text = "${String.format("%.1f", percentage)}% (₹${
                        String.format(
                            "%.0f",
                            entry.value
                        )
                    })",
                    fontSize = 13.sp,
                    color = colorResource(R.color.soft_blue_dark)
                )
            }
        }
    }
}
