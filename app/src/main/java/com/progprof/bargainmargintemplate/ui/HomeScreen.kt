package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

@Composable
fun HomeScreen(
    budgetViewModel: BudgetViewModel,
    navController: NavController
) {
    val uiState by budgetViewModel.uiState.collectAsState()
    val selectedMonthWithWeeks = uiState.selectedMonthWithWeeks
    val currentWeekNumber = uiState.currentWeekNumber
    val expensesForCurrentWeek = uiState.expensesForCurrentWeek

    if (selectedMonthWithWeeks == null || selectedMonthWithWeeks.weeks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No budget set for this month.")
                Text("Select 'Edit Monthly Budget' from the menu to begin.")
            }
        }
        return
    }

    val month = selectedMonthWithWeeks.month
    val weeks = selectedMonthWithWeeks.weeks
    val currentWeek = weeks.find { it.weekNumber == currentWeekNumber }

    if (currentWeek == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
            Text("Loading week data...", modifier = Modifier.padding(top = 60.dp))
        }
        return
    }

    val totalMonthlyBudget = month.totalBudget
    val spentMonthly = month.totalSpent
    val monthRemaining = totalMonthlyBudget - spentMonthly
    val monthlyProgress = if (totalMonthlyBudget > 0) (monthRemaining / totalMonthlyBudget).toFloat() else 0f

    val totalWeeklyBudget = currentWeek.weekBudget
    val spentWeekly = currentWeek.weekSpent
    val weekRemaining = totalWeeklyBudget - spentWeekly
    val weeklyProgress = if (totalWeeklyBudget > 0) (weekRemaining / totalWeeklyBudget).toFloat() else 0f

    val monthlyGoal = uiState.currentMonthGoal
    val weeklyGoal = uiState.currentWeekGoal

    // Calculate goal marker positions. This represents how much of the budget should be *remaining* to meet the goal.
    val monthlyGoalProgress = if (totalMonthlyBudget > 0) (monthlyGoal / totalMonthlyBudget).toFloat() else 0f
    val weeklyGoalProgress = if (totalWeeklyBudget > 0) (weeklyGoal / totalWeeklyBudget).toFloat() else 0f

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
            text = "$${"%.2f".format(monthRemaining)} / $${"%.2f".format(totalMonthlyBudget)}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        BoxWithConstraints(
            modifier = Modifier
                .height(26.dp)
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .clip(MaterialTheme.shapes.extraSmall)
        ) {
            LinearProgressIndicator(
                progress = { monthlyProgress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxSize()
            )
            if (monthlyGoal > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(4.dp)
                        .align(Alignment.CenterStart)
                        .offset(x = maxWidth * monthlyGoalProgress)
                        .background(Color.White)
                )
            }
        }


        // ... Text for weekly budget is unchanged ...
        Text(text = "Week $currentWeekNumber Budget Remaining: ", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "$${"%.2f".format(weekRemaining)} / $${"%.2f".format(totalWeeklyBudget)}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )


        BoxWithConstraints(
            modifier = Modifier
                .height(26.dp)
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .clip(MaterialTheme.shapes.extraSmall)
        ) {
            LinearProgressIndicator(
                progress = { weeklyProgress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxSize()
            )
            // Add the weekly goal marker
            if (weeklyGoal > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(4.dp)
                        .align(Alignment.CenterStart)
                        .offset(x = maxWidth * weeklyGoalProgress)
                        .background(Color.White)
                )
            }
        }

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
