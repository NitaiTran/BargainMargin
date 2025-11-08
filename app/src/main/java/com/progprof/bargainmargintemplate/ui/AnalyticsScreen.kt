package com.progprof.bargainmargintemplate.ui

import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AnalyticsScreen(categoryList: List<Category>, totalBudget: Double) {
    // --- CHANGE: Collect the state from the ViewModel ---
    //val totalBudgetValue by budgetViewModel.totalRemainingBudget.collectAsState()
    //val numCategories by budgetViewModel.myNumberOfCategories.collectAsState()
    //val totalBudgetValue = 1500.0
    val totalBudgetValue = totalBudget
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        DrawPieChart(
            modifier = Modifier.size(300.dp),
            totalBudget = totalBudgetValue,
            categoryList = categoryList
        )

        DrawAllPercentageBars(categoryList, totalBudgetValue)
    }
}

@Composable
fun DrawPieChart(modifier: Modifier = Modifier, totalBudget: Double, categoryList: List<Category>) {
    // CHANGE: This composable no longer needs the whole ViewModel.
    // It receives the exact data it needs, which makes it more reusable.
    var startAngle = -90f
    val totalBudgetFloat = totalBudget.toFloat().coerceAtLeast(1f)
    var budgetAllocated = 0f
    //val categoryBudget = totalBudgetFloat / numberOfCategories

    val categoryBudget = if (categoryList.isEmpty()) totalBudgetFloat else totalBudgetFloat / categoryList.size

    val colorsArray = arrayOf(
        Color(0xFF3B82F6), Color(0xFFF59E0B), Color(0xFF10B981), Color(0xFF06B6D4),
        Color(0xFFEC4899), Color(0xFF14B8A6), Color(0xFF9333EA), Color(0xFF6366F1)
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {

            categoryList.forEachIndexed { index, category ->
                val sweepAngle = 360f * (category.totalBudget.toFloat() / totalBudgetFloat)
                val arcColor = colorsArray[index % colorsArray.size]
                budgetAllocated += category.totalBudget.toFloat()

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

            drawArc(
                color = Color.Gray,
                startAngle = startAngle,
                sweepAngle = 360f * ((totalBudgetFloat - budgetAllocated) / totalBudgetFloat),
                useCenter = false,
                size = Size(size.width, size.height),
                style = Stroke(width = 40f, cap = StrokeCap.Butt)
            )
        }
        Text(text = "$${"%.2f".format(totalBudget)}", fontSize = 24.sp)
    }
}

@Composable
fun DrawAllPercentageBars(categoryList: List<Category>, totalBudget: Double) {
    // CHANGE: This composable also receives only the data it needs.
    if (categoryList.isEmpty())
        return

    val colorsArray = arrayOf(
        Color(0xFF3B82F6), Color(0xFFF59E0B), Color(0xFF10B981), Color(0xFF06B6D4),
        Color(0xFFEC4899), Color(0xFF14B8A6), Color(0xFF9333EA), Color(0xFF6366F1)
    )

    Column {
        categoryList.forEachIndexed { index, category ->
            val barColor = colorsArray[index % colorsArray.size]
            DrawBudgetPercentBar((category.totalBudget / totalBudget).toFloat(), barColor, category.categoryName)
        }
    }

//    val percentage = 1f / numberOfCategories
//    Column {
//        for (i in 0 until numberOfCategories) {
//            val barColor = colorsArray[i % colorsArray.size]
//            DrawBudgetPercentBar(percentage, barColor, "Category ${i + 1}")
//        }
//    }
}

@Composable
fun DrawBudgetPercentBar(percentage: Float, color: Color, label: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.width(100.dp))
        Box(modifier = Modifier.weight(percentage).height(20.dp).background(color))
        Text(text = " ${"%.0f".format(percentage * 100)}%", modifier = Modifier.padding(start = 8.dp))
    }
}
