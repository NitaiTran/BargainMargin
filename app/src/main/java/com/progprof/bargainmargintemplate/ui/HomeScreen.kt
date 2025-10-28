package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable // Define the HomeScreen composable function
fun HomeScreen(navController: NavController, budgetViewModel: BudgetViewModel) { // Pass the NavController and BudgetViewModel
    val monthlyBudget = budgetViewModel.monthlyRemainingBudget //get monthly budget from viewmodel
    val currentWeekRemainingBudget = budgetViewModel.getCurrentWeekRemainingBudget()
    val currentWeekTotalBudget = budgetViewModel.getCurrentWeekTotalBudget()
    val totalBudget = budgetViewModel.totalRemainingBudget

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Add padding
        horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
        verticalArrangement = Arrangement.spacedBy(12.dp) // Add vertical spacing
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium) //Title
        Text(text = "Monthly Budget Remaining: ", style = MaterialTheme.typography.titleMedium) //Budget Title:
        Text( //Display monthly budget

            text = "$%.2f".format(monthlyBudget) + "/%.2f".format(totalBudget), // Format the budget as a currency string
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp) // Add bottom padding
        )

        LinearProgressIndicator(
            progress = { (monthlyBudget / totalBudget).toFloat() },
            modifier = Modifier.height(26.dp).fillMaxWidth().padding(bottom = 10.dp),
            color = MaterialTheme.colorScheme.secondary
        ){}

        Text(text = "Week ${budgetViewModel.getCurrentWeek()} Budget Remaining: ", style = MaterialTheme.typography.titleMedium) //Budget Title:
        Text( //Display monthly budget

            text = "$%.2f".format(currentWeekRemainingBudget) + "/%.2f".format(currentWeekTotalBudget), // Format the budget as a currency string
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp) // Add bottom padding
        )

        LinearProgressIndicator(
            progress = { (currentWeekRemainingBudget / currentWeekTotalBudget).toFloat() },
            modifier = Modifier.height(26.dp).fillMaxWidth().padding(bottom = 10.dp),
            color = MaterialTheme.colorScheme.primary
        ){}

        Button(onClick = { navController.navigate(ScreenController.Screen.MainBudgetEntry.name) }) {
            Text(if(monthlyBudget > 0) "Edit Monthly Budget" else "Set Up Budget") // Your old MainBudgetEntry screen
        }
        Button(onClick = { navController.navigate(ScreenController.Screen.ExpenseTracker.name) }) {
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