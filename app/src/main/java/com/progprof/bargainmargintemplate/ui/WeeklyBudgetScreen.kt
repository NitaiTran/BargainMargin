package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
fun WeeklyBudgetScreen(
    navController: NavHostController,
    budgetViewModel: BudgetViewModel,
) {
    val budget by budgetViewModel.budgetState.collectAsState()
    var week1Input by remember { mutableStateOf("") }
    var week2Input by remember { mutableStateOf("") }
    var week3Input by remember { mutableStateOf("") }
    var week4Input by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(budget) {
        week1Input = if (budget.week1TotalBudget > 0) budget.week1TotalBudget.toString() else ""
        week2Input = if (budget.week2TotalBudget > 0) budget.week2TotalBudget.toString() else ""
        week3Input = if (budget.week3TotalBudget > 0) budget.week3TotalBudget.toString() else ""
        week4Input = if (budget.week4TotalBudget > 0) budget.week4TotalBudget.toString() else ""
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Edit Weekly Budgets", style = MaterialTheme.typography.headlineMedium)
        Text("Current Total Budget: $${String.format("%.2f", budget.totalBudget)}")

        Spacer(modifier = Modifier.height(16.dp))

        // Week 1
        TextField(
            value = week1Input,
            onValueChange = { week1Input = it },
            label = { Text("Week 1 Total Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Week 2
        TextField(
            value = week2Input,
            onValueChange = { week2Input = it },
            label = { Text("Week 2 Total Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Week 3
        TextField(
            value = week3Input,
            onValueChange = { week3Input = it },
            label = { Text("Week 3 Total Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Week 4
        TextField(
            value = week4Input,
            onValueChange = { week4Input = it },
            label = { Text("Week 4 Total Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage != null) {
            Text(errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                val newWeek1 = week1Input.toDoubleOrNull()
                val newWeek2 = week2Input.toDoubleOrNull()
                val newWeek3 = week3Input.toDoubleOrNull()
                val newWeek4 = week4Input.toDoubleOrNull()
                when {
                    newWeek1 == null || newWeek1 < 0 -> errorMessage = "Please enter a valid amount for Week 1."
                    newWeek2 == null || newWeek2 < 0 -> errorMessage = "Please enter a valid amount for Week 2."
                    newWeek3 == null || newWeek3 < 0 -> errorMessage = "Please enter a valid amount for Week 3."
                    newWeek4 == null || newWeek4 < 0 -> errorMessage = "Please enter a valid amount for Week 4."
                    else -> {
                        errorMessage = null
                        budgetViewModel.alterWeeklyBudgets(newWeek1, newWeek2, newWeek3, newWeek4)
                        navController.navigate(ScreenController.Screen.Home.name) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}
