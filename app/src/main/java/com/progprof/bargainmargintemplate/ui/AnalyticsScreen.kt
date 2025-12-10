package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnalyticsScreen(
    budgetViewModel: BudgetViewModel
) {
    val uiState by budgetViewModel.uiState.collectAsState()
    val categoryList by budgetViewModel.categories.collectAsState()

    val month = uiState.selectedMonthWithWeeks?.month

    if (month == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
            Text("No budget data for this month.", modifier = Modifier.padding(top = 60.dp))
        }
        return
    }

    val totalMonthlyBudget = month.totalBudget

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
    ) {
        DrawPieChart(
            modifier = Modifier.size(300.dp),
            categoryList = categoryList,
            totalMonthlyBudget = totalMonthlyBudget
        )

        DrawAllPercentageBars(
            categoryList = categoryList,
            totalMonthlyBudget = totalMonthlyBudget
        )
    }
}

@Composable
fun DrawPieChart(modifier: Modifier = Modifier, categoryList: List<Category>, totalMonthlyBudget: Double) {
    var startAngle = -90f

    val totalAllocatedToCategories = categoryList.sumOf { it.totalBudget }

    val colorsArray = arrayOf(
        Color(0xFF3B82F6), Color(0xFFF59E0B), Color(0xFF10B981), Color(0xFF06B6D4),
        Color(0xFFEC4899), Color(0xFF14B8A6), Color(0xFF9333EA), Color(0xFF6366F1)
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            if (categoryList.isEmpty()) {
                drawArc(
                    color = Color.LightGray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 40f, cap = StrokeCap.Butt)
                )
            } else {
                categoryList.forEachIndexed { index, category ->
                    val sweepAngle = 360f * (category.totalBudget.toFloat() / totalMonthlyBudget.toFloat())
                    val arcColor = colorsArray[index % colorsArray.size]

                    drawArc(
                        color = arcColor,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        size = Size(size.width, size.height),
                        style = Stroke(width = 40f, cap = StrokeCap.Butt)
                    )
                    startAngle += sweepAngle
                }

                val unallocatedBudget = totalMonthlyBudget - totalAllocatedToCategories
                if (unallocatedBudget > 0) {
                    val unallocatedSweep = 360f * (unallocatedBudget.toFloat() / totalMonthlyBudget.toFloat())
                    drawArc(
                        color = Color.LightGray,
                        startAngle = startAngle,
                        sweepAngle = unallocatedSweep,
                        useCenter = false,
                        size = Size(size.width, size.height),
                        style = Stroke(width = 40f, cap = StrokeCap.Butt)
                    )
                }
            }
        }

        Text(text = "$${"%.2f".format(categoryList.sumOf { it.budgetRemaining })}", fontSize = 24.sp)
    }
}

@Composable
fun DrawAllPercentageBars(categoryList: List<Category>, totalMonthlyBudget: Double) {
    if (categoryList.isEmpty()) {
        Text("No categories have been added yet.")
        return
    }

    val totalBudgetFloat = totalMonthlyBudget.toFloat().coerceAtLeast(1f)
    val colorsArray = arrayOf(
        Color(0xFF3B82F6), Color(0xFFF59E0B), Color(0xFF10B981), Color(0xFF06B6D4),
        Color(0xFFEC4899), Color(0xFF14B8A6), Color(0xFF9333EA), Color(0xFF6366F1)
    )

    Column {
        categoryList.forEachIndexed { index, category ->
            val barColor = colorsArray[index % colorsArray.size]
            val percentage = (category.budgetRemaining / category.totalBudget).toFloat()
            DrawBudgetPercentBar(percentage, barColor, category.categoryName)
        }
    }
}
@Composable
fun DrawBudgetPercentBar(percentage: Float, color: Color, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.width(120.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = percentage.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(color)
            )
        }
        //Text(text = " ${"%.0f".format(percentage * 100)}%", modifier = Modifier.padding(start = 8.dp))
    }
}
