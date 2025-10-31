package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState


@Composable
fun HomeScreen(
    monthlyBudget: Double,
    totalBudget: Double
) {
    // Prevent division by zero if the total budget isn't set yet.
    val monthlyProgress = if (totalBudget > 0) (monthlyBudget / totalBudget).toFloat() else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Add padding
        horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
        verticalArrangement = Arrangement.spacedBy(12.dp) // Add vertical spacing
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium) //Title
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Monthly Budget Remaining: ", style = MaterialTheme.typography.titleMedium) //Budget Title:

        Text( //Display monthly budget
            text = "$${"%.2f".format(monthlyBudget)} / $${"%.2f".format(totalBudget)}", // Format the budget as a currency string
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp) // Add bottom padding
        )

        LinearProgressIndicator(
            progress = { monthlyProgress }, // The lambda now correctly uses the calculated progress
            modifier = Modifier
                .height(26.dp)
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
    }
}
