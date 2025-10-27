package com.progprof.bargainmargintemplate.ui

import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    val colorsArray = arrayOf(Color.Cyan, Color.Red, Color.Magenta, Color.Blue)

    // check to see if there's an even number of categories
    val evenNumberOfCategories = (budgetViewModel.myNumberOfCategories % 2 == 0)

    var arcColor = colorsArray[0]
    Canvas(modifier = modifier) {
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
}