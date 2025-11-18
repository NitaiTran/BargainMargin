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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun HomeScreen(
    budgetViewModel: BudgetViewModel,
    navController: NavController
) {
    val uiState by budgetViewModel.uiState.collectAsState()
    val monthWithWeeks = uiState.monthWithWeeks
    val currentWeekNumber = uiState.currentWeekNumber
    val expensesForCurrentWeek = uiState.expensesForCurrentWeek

    if (monthWithWeeks == null) {
        LaunchedEffect(Unit) {
            navController.navigate(ScreenController.Screen.MainBudgetEntry.name) {
                popUpTo(ScreenController.Screen.Home.name) { inclusive = true }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val month = monthWithWeeks.month
    val weeks = monthWithWeeks.weeks
    val currentWeek = weeks.find { it.weekNumber == currentWeekNumber }

    if (currentWeek == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val totalMonthlyBudget = month.totalBudget
    val spentMonthly = month.totalSpent
    val remainingMonthly = totalMonthlyBudget - spentMonthly
    val monthlyProgress = if (totalMonthlyBudget > 0) (remainingMonthly / totalMonthlyBudget).toFloat() else 0f

    val totalWeeklyBudget = currentWeek.weekBudget
    val spentWeekly = currentWeek.weekSpent
    val remainingWeekly = totalWeeklyBudget - spentWeekly
    val weeklyProgress = if (totalWeeklyBudget > 0) (remainingWeekly / totalWeeklyBudget).toFloat() else 0f

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
            text = "$${"%.2f".format(remainingMonthly)} / $${"%.2f".format(totalMonthlyBudget)}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        LinearProgressIndicator(
            progress = { monthlyProgress.coerceIn(0f, 1f) },
            modifier = Modifier
                .height(26.dp)
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        Text(text = "Week $currentWeekNumber Budget Remaining: ", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "$${"%.2f".format(remainingWeekly)} / $${"%.2f".format(totalWeeklyBudget)}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        LinearProgressIndicator(
            progress = { weeklyProgress.coerceIn(0f, 1f) },
            modifier = Modifier
                .height(26.dp)
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("Expenses This Week", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        if (expensesForCurrentWeek.isEmpty()) {
            Text("No expenses this week", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                items(expensesForCurrentWeek, key = { it.id }) { expense ->
                    Text(
                        text = "${expense.descriptionOfExpense}: $${"%.2f".format(expense.amountOfExpense)} (${expense.categoryOfExpense})",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}