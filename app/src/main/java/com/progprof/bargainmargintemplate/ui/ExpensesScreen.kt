package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ExpensesScreen(
    budgetViewModel: BudgetViewModel,
    navController: NavController
) {
    val budget by budgetViewModel.budgetState.collectAsState()
    val allExpenses by budgetViewModel.expenses.collectAsState()
    val totalBudget = budget.totalBudget
    val monthlyRemaining = budget.monthlyRemainingBudget
    val currentWeek = budget.myCurrentWeek
    val weeklyTotal = when (currentWeek) {
        1 -> budget.week1TotalBudget
        2 -> budget.week2TotalBudget
        3 -> budget.week3TotalBudget
        4 -> budget.week4TotalBudget
        else -> 0.0
    }
    val weeklyRemaining = when (currentWeek) {
        1 -> budget.week1RemainingBudget
        2 -> budget.week2RemainingBudget
        3 -> budget.week3RemainingBudget
        4 -> budget.week4RemainingBudget
        else -> 0.0
    }

    var expenseInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("") }
    var weekInput by remember { mutableStateOf(currentWeek.toString()) } // Default to current week
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentWeek) {
        weekInput = currentWeek.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            item {
                Text("Expense Tracker", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                // Monthly budget info
                Text("Total Monthly Budget: $${"%.2f".format(totalBudget)}")
                Text(
                    text = "Monthly Remaining: $${"%.2f".format(monthlyRemaining)}",
                    color = if (monthlyRemaining < 0) MaterialTheme.colorScheme.error else Color.Unspecified
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Weekly budget info
                Text("Week $currentWeek Budget: $${"%.2f".format(weeklyTotal)}")
                Text(
                    text = "Week $currentWeek Remaining: $${"%.2f".format(weeklyRemaining)}",
                    color = if (weeklyRemaining < 0) MaterialTheme.colorScheme.error else Color.Unspecified
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Input Fields
                TextField(
                    value = expenseInput,
                    onValueChange = { expenseInput = it },
                    label = { Text("Expense Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = descriptionInput,
                    onValueChange = { descriptionInput = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = weekInput,
                    onValueChange = { weekInput = it },
                    label = { Text("Week # (1-4)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage != null) {
                    Text(
                        errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Add Expense Button
                Button(
                    onClick = {
                        val amount = expenseInput.toDoubleOrNull()
                        val week = weekInput.toIntOrNull()
                        when {
                            amount == null || amount <= 0 -> errorMessage =
                                "Please enter a valid expense amount."
                            descriptionInput.isBlank() -> errorMessage = "Description cannot be empty."
                            week == null || week !in 1..4 -> errorMessage =
                                "Please enter a valid week (1-4)."
                            else -> {
                                budgetViewModel.addExpense(amount, descriptionInput, categoryInput, week)

                                errorMessage = null
                                expenseInput = ""
                                descriptionInput = ""
                                // categoryInput = "" // Optional: you might want to keep the category
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Expense")
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("All Expenses:", style = MaterialTheme.typography.titleMedium)
            }
            if (allExpenses.isEmpty()) {
                item {
                    Text("No expenses yet.", modifier = Modifier.padding(top = 8.dp))
                }
            } else {
                items(allExpenses, key = { it.id }) { expense ->
                    ExpenseItem(expense = expense, onRemoveClick = { budgetViewModel.removeExpense(expense) })
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense, onRemoveClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.descriptionOfExpense, style = MaterialTheme.typography.bodyLarge)
                Text("Week: ${expense.weekOfExpense}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text("-$${"%.2f".format(expense.amountOfExpense)}", color = MaterialTheme.colorScheme.error)
            TextButton(onClick = onRemoveClick) {
                Text("Remove")
            }
        }
    }
}
