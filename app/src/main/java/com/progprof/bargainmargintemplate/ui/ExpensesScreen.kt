package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    // --- THIS IS THE FIX ---
    // 1. Collect all StateFlows from the ViewModel to get their actual values.
    val totalBudget by budgetViewModel.totalRemainingBudget.collectAsState()
    val monthlyRemaining by budgetViewModel.monthlyRemainingBudget.collectAsState()
    val allExpenses by budgetViewModel.expenses.collectAsState()
    val numberOfCategories by budgetViewModel.myNumberOfCategories.collectAsState()

    // 2. Derive weekly budget logic here in the UI, as it's a display concern.

    val weeklyBudget = budgetViewModel.getCurrentWeekTotalBudget()
    val expensesThisWeek = allExpenses.filter { it.weekOfExpense == 1 } // Example for week 1
    val weeklySpent = expensesThisWeek.sumOf { it.amountOfExpense }
    val weeklyRemaining = budgetViewModel.getCurrentWeekRemainingBudget()

    // Local state for the text input fields
    var expenseInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("") }
    var weekInput by remember { mutableStateOf("1") } // Default to week 1
    var errorMessage by remember { mutableStateOf<String?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Use a Column with weight to push the button to the bottom
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
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
            Text("This Week's Budget: $${"%.2f".format(weeklyBudget)}")
            Text(
                text = "This Week's Remaining: $${"%.2f".format(weeklyRemaining)}",
                color = if (weeklyRemaining < 0) MaterialTheme.colorScheme.error else Color.Unspecified
            )
            Spacer(modifier = Modifier.height(24.dp))

            // --- Input Fields ---
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
                Text(errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Add Expense Button
            Button(
                onClick = {
                    val amount = expenseInput.toDoubleOrNull()
                    val week = weekInput.toIntOrNull()
                    when {
                        amount == null || amount <= 0 -> errorMessage = "Please enter a valid expense amount."
                        descriptionInput.isBlank() -> errorMessage = "Description cannot be empty."
                        week == null || week !in 1..4 -> errorMessage = "Please enter a valid week (1-4)."
                        else -> {
                            budgetViewModel.changeCurrentWeek(week)
                            budgetViewModel.addExpense(amount, descriptionInput, categoryInput, week)

                            //budgetViewModel.calculateWeeklyBudget(amount)
                            // Clear inputs
                            errorMessage = null
                            expenseInput = ""
                            descriptionInput = ""
                            categoryInput = ""
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Expense")
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- List of Expenses ---
            Text("All Expenses:", style = MaterialTheme.typography.titleMedium)
            if (allExpenses.isEmpty()) {
                Text("No expenses yet.")
            } else {
                allExpenses.forEach { expense ->
                    ExpenseItem(expense = expense, onRemoveClick = { budgetViewModel.removeExpense(expense) })
                }
            }
        }

        // --- Back Button at the bottom ---

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
