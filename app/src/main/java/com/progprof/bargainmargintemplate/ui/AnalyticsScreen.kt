package com.progprof.bargainmargintemplate.ui

import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AnalyticsScreen(navController: NavController, budgetViewModel: BudgetViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Analytics Screen")

        DrawPieChart(
            modifier = Modifier.size(300.dp),
            budgetViewModel)

        DrawAllPercentageBars(budgetViewModel)

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back to Home")
        }
    }
}

@Composable
fun DrawPieChart(modifier: Modifier = Modifier, budgetViewModel: BudgetViewModel) {
    var startAngle = -90f
    val totalBudget = budgetViewModel.totalBudget.toFloatOrNull() ?: 1f

    // right now we have no way of tracking how much is allotted to a category
    // so I have to hardcode it
    val categoryBudget = totalBudget / budgetViewModel.myNumberOfCategories

    // we can also freely add more colors here or let the user customize the color
    // of a category themselves
    val colorsArray = arrayOf(
        Color(0xFF3B82F6),
        Color(0xFFF59E0B),
        Color(0xFF10B981),
        Color(0xFF06B6D4),
        Color(0xFFEC4899),
        Color(0xFF14B8A6),
        Color(0xFF9333EA),
        Color(0xFF6366F1)
    )

    // check to see if there's an even number of categories
    val evenNumberOfCategories = (budgetViewModel.myNumberOfCategories % 2 == 0)

    var arcColor: Color
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            // loop over the number of categories, drawing an arc for each one
            // when we add tracking category budget amounts, we can calculate the
            // sweep angle with that value rather than a hardcoded value
            for (i in 0 until budgetViewModel.myNumberOfCategories) {
                // scale how much the arc will take via (currentSlice / total)
                val sweepAngle = 360f * (categoryBudget / totalBudget)

                arcColor = if (i == budgetViewModel.myNumberOfCategories - 1 && !evenNumberOfCategories)
                    colorsArray[(i % colorsArray.size) + 1]
                else
                    colorsArray[i % colorsArray.size]

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
        }

        Text(text = "$" + budgetViewModel.totalBudget, fontSize = 24.sp)

    }
}

@Composable
fun DrawAllPercentageBars(budgetViewModel: BudgetViewModel)
{
    val colorsArray = arrayOf(
        Color(0xFF3B82F6),
        Color(0xFFF59E0B),
        Color(0xFF10B981),
        Color(0xFF06B6D4),
        Color(0xFFEC4899),
        Color(0xFF14B8A6),
        Color(0xFF9333EA),
        Color(0xFF6366F1)
    )

    val evenNumberOfCategories = (budgetViewModel.myNumberOfCategories % 2 == 0)

    var barColor = colorsArray[0]

    val percentage = 1f / budgetViewModel.myNumberOfCategories
    for (i in 0 until budgetViewModel.myNumberOfCategories) {

        barColor = if (i == budgetViewModel.myNumberOfCategories - 1 && !evenNumberOfCategories)
            colorsArray[(i % colorsArray.size) + 1]
        else
            colorsArray[i % colorsArray.size]

        DrawBudgetPercentBar(percentage, barColor)
    }
}

@Composable
fun DrawBudgetPercentBar(percentage: Float, color: Color)
{
    Row(modifier = Modifier) {
        Text(text = "${percentage * 100}%")
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.width((percentage * 200).dp).height(20.dp).background(color))
    }
}