package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    budgetViewModel: BudgetViewModel,
    navController: NavController
) {
    val budget by budgetViewModel.budgetState.collectAsState()

    if (budget.totalBudget <= 0.0) {
        LaunchedEffect(budget.totalBudget) {
            if (budget.totalBudget <= 0.0) {
                navController.navigate(ScreenController.Screen.MainBudgetEntry.name) {
                    popUpTo(ScreenController.Screen.Home.name) { inclusive = true }
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val monthlyBudget = budget.monthlyRemainingBudget
    val totalBudget = budget.totalBudget
    val currentWeek = budget.myCurrentWeek
    val weeklyRemainingBudget = when (currentWeek) {
        1 -> budget.week1RemainingBudget
        2 -> budget.week2RemainingBudget
        3 -> budget.week3RemainingBudget
        4 -> budget.week4RemainingBudget
        else -> 0.0
    }
    val weeklyTotalBudget = when (currentWeek) {
        1 -> budget.week1TotalBudget
        2 -> budget.week2TotalBudget
        3 -> budget.week3TotalBudget
        4 -> budget.week4TotalBudget
        else -> 0.0
    }
    val monthlyProgress = if (totalBudget > 0) (monthlyBudget / totalBudget).toFloat() else 0f
    val weeklyProgress = if (weeklyTotalBudget > 0) (weeklyRemainingBudget / weeklyTotalBudget).toFloat() else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Monthly Budget Remaining: ", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "$${"%.2f".format(monthlyBudget)} / $${"%.2f".format(totalBudget)}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        LinearProgressIndicator(
            progress = { monthlyProgress },
            modifier = Modifier
                .height(26.dp)
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        Text(text = "Week $currentWeek Budget Remaining: ", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "$%.2f".format(weeklyRemainingBudget) + "/%.2f".format(weeklyTotalBudget),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        LinearProgressIndicator(
            progress = { weeklyProgress },
            modifier = Modifier
                .height(26.dp)
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
