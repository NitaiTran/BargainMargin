package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable // Define the HomeScreen composable function
fun HomeScreen(navController: NavController, budgetViewModel: BudgetViewModel) { // Pass the NavController and BudgetViewModel
    val monthlyBudget = budgetViewModel.monthlyRemainingBudget //get monthly budget from viewmodel

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp), // Add padding
        horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
        verticalArrangement = Arrangement.spacedBy(12.dp) // Add vertical spacing
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium) //Title
        Text(text = "Monthly Budget: ", style = MaterialTheme.typography.titleMedium) //Budget Title:
        Text( //Display monthly budget

            text = "$%.2f".format(monthlyBudget), // Format the budget as a currency string
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp) // Add bottom padding
        )

        Button(onClick = { navController.navigate(ScreenController.Screen.MainBudgetEntry.name) }) {
            Text(if(monthlyBudget > 0) "Edit Monthly Budget" else "Set Up Budget") // Your old MainBudgetEntry screen
        }
        Button(onClick = { navController.navigate(ScreenController.Screen.ExpenseTracking.name) }) {
            Text("Expense Tracking")
        }
        Button(onClick = { navController.navigate(ScreenController.Screen.Categories.name) }) {
            Text("Categories")
        }
        Button(onClick = { navController.navigate(ScreenController.Screen.Analytics.name) }) {
            Text("Analytics")
        }
        Button(onClick = { navController.navigate(ScreenController.Screen.Settings.name) }) {
            Text("Settings")
        }

    }
}