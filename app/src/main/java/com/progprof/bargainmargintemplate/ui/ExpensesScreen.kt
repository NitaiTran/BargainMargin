package com.progprof.bargainmargintemplate.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ExpensesScreen(
    budgetViewModel: BudgetViewModel,
    navController: NavController
) {
    var expenseInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("") }
    var weekInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val remainingBudget = budgetViewModel.monthlyRemainingBudget
    val expenses = budgetViewModel.expenses

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Text(
            text = "Expense Tracker",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Budget info
        Text("Total Budget: $${"%.2f".format(budgetViewModel.totalRemainingBudget)}")
        Text(
            text = "Remaining Budget: $${"%.2f".format(remainingBudget)}",
            color = if (remainingBudget < 0) Color.Red else Color.Unspecified,

        )

        if (remainingBudget < 0) {
            Text(
                text = "You've exceeded your monthly budget!",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 20.dp).padding(top = 4.dp)

            )
        }

        Text("Week ${budgetViewModel.getCurrentWeek()} Budget: $${"%.2f".format(budgetViewModel.getCurrentWeekTotalBudget())}")
        Text(
            text = "Week ${budgetViewModel.getCurrentWeek()} Remaining Budget: $${"%.2f".format(budgetViewModel.getCurrentWeekRemainingBudget())}",
            color = if (budgetViewModel.getCurrentWeekRemainingBudget() < 0) Color.Red else Color.Unspecified
        )

        if (budgetViewModel.getCurrentWeekRemainingBudget() < 0) {
            Text(
                text = "You've exceeded your weekly budget!",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Expense amount input
        TextField(
            value = expenseInput,
            onValueChange = { expenseInput = it },
            label = { Text("Expense Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description input
        TextField(
            value = descriptionInput,
            onValueChange = { descriptionInput = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Category input
        TextField(
            value = categoryInput,
            onValueChange = { categoryInput = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Week input
        TextField(
            value = weekInput,
            onValueChange = { weekInput = it },
            label = { Text("Week #") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Error message
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // Add expense button
        Button(
            onClick = {
                val amount = expenseInput.toDoubleOrNull()
                val week = weekInput.toIntOrNull()

                when {
                    amount == null || amount <= 0 -> {
                        errorMessage = "Please enter a valid expense amount greater than 0."
                    }
                    descriptionInput.isBlank() -> {
                        errorMessage = "Description cannot be empty."
                    }

                    week == null || week < 1 || week > 4 -> {
                        errorMessage = "Must enter a valid week number."
                    }

                    else -> {
                        budgetViewModel.changeCurrentWeek(week)
                        budgetViewModel.addExpense(amount, descriptionInput, categoryInput, week)
                        budgetViewModel.calculateWeeklyBudget(amount)
                        errorMessage = null
                        expenseInput = ""
                        descriptionInput = ""
                        categoryInput = ""
                        weekInput = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Expense")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List of expenses
        Text("Expenses:", style = MaterialTheme.typography.titleMedium)
        if (expenses.isEmpty()) {
            Text("No expenses yet.")
        } else {
            expenses.forEach { expense ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6F6))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(expense.descriptionOfExpense)
                            if (expense.categoryOfExpense.isNotEmpty()) {
                                Text(
                                    "Category: ${expense.categoryOfExpense}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }


                            Text("Week: ${expense.weekOfExpense}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )


                        }
                        Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {

                            Text(
                                text = "-$${"%.2f".format(expense.amountOfExpense)}",
                                color = Color.Red
                            )
                            TextButton(
                                onClick = {
                                    budgetViewModel.removeExpense(expense) },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Remove", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier.padding(75.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom

    ) {
        Button(onClick = { navController.popBackStack() }) {

            Text(text = "Back to Home")
        }
    }

}
