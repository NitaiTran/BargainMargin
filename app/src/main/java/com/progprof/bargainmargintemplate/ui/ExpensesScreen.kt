package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    budgetViewModel: BudgetViewModel,
    navController: NavController
) {
    val uiState by budgetViewModel.uiState.collectAsState()
    val allCategories by budgetViewModel.categories.collectAsState()

    val monthWithWeeks = uiState.selectedMonthWithWeeks
    val currentWeekNumber = uiState.currentWeekNumber
    val expensesForCurrentWeek = uiState.expensesForCurrentWeek
    val isAddingExpense = uiState.isAddingExpense

    val month = monthWithWeeks?.month
    val currentWeek = monthWithWeeks?.weeks?.find { it.weekNumber == currentWeekNumber }
    var expenseInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedWeekForExpense by remember { mutableStateOf(currentWeekNumber) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(isAddingExpense) {
        if (!isAddingExpense) {
            expenseInput = ""
            descriptionInput = ""
            selectedCategory = null
            errorMessage = null
        }
    }

    LaunchedEffect(currentWeekNumber) {
        selectedWeekForExpense = currentWeekNumber
    }

    if (month == null || currentWeek == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
            Text("No data for selected month. Please set a budget.", Modifier.padding(top = 60.dp))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text("Expense Tracker", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Total Monthly Budget: $${"%.2f".format(month.totalBudget)}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Monthly Remaining: $${"%.2f".format(month.totalBudget - month.totalSpent)}",
                color = if ((month.totalBudget - month.totalSpent) < 0) MaterialTheme.colorScheme.error else Color.Unspecified,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Week $currentWeekNumber Budget: $${"%.2f".format(currentWeek.weekBudget)}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Week $currentWeekNumber Remaining: $${"%.2f".format(currentWeek.weekBudget - currentWeek.weekSpent)}",
                color = if ((currentWeek.weekBudget - currentWeek.weekSpent) < 0) MaterialTheme.colorScheme.error else Color.Unspecified,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

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
            WeekDropdown(
                selectedWeek = selectedWeekForExpense,
                budgetViewModel = budgetViewModel
            )
            Spacer(modifier = Modifier.height(8.dp))
            CategoryDropdown(
                categories = allCategories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (errorMessage != null) {
                Text(errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val amount = expenseInput.toDoubleOrNull()
                    if (isAddingExpense) {
                    } else if (amount == null || amount <= 0) {
                        errorMessage = "Please enter a valid expense amount."
                    } else if (selectedCategory == null) {
                        errorMessage = "Please select a category."
                    } else {
                        if(descriptionInput.isBlank())
                        {
                            descriptionInput = "[No Description]"
                        }
                        budgetViewModel.addExpense(amount, descriptionInput, selectedCategory!!.categoryName, selectedWeekForExpense)
                        budgetViewModel.updateCategoryRemainingBudget(selectedCategory!!, -amount)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isAddingExpense) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Add Expense")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Expenses this Week:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            if (expensesForCurrentWeek.isEmpty()) {
                item {
                    Text("No expenses yet for week $currentWeekNumber.", modifier = Modifier.padding(top = 8.dp))
                }
            } else {
                items(expensesForCurrentWeek, key = { it.id }) { expense ->
                    ExpenseItem(
                        expense = expense,
                        onRemoveClick = {
                            budgetViewModel.removeExpense(expense)
                            budgetViewModel.updateCategoryRemainingBudget(budgetViewModel.findCategoryByName(expense.categoryOfExpense)!!, expense.amountOfExpense)
                        }
                    )
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
                Text("Category: ${expense.categoryOfExpense}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text("-$${"%.2f".format(expense.amountOfExpense)}", color = MaterialTheme.colorScheme.error)
            TextButton(onClick = onRemoveClick) {
                Text("Remove")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        TextField(
            value = selectedCategory?.categoryName ?: "Select a Category",
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            if (categories.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No categories available. Add one on the Categories screen.") },
                    onClick = { isExpanded = false },
                    enabled = false
                )
            } else {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.categoryName) },
                        onClick = {
                            onCategorySelected(category)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekDropdown(
    selectedWeek: Int,
    budgetViewModel: BudgetViewModel,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val weeks = (1..4).toList()

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        TextField(
            value = "Week $selectedWeek",
            onValueChange = {},
            readOnly = true,
            label = { Text("Week for Expense") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            weeks.forEach { weekNumber ->
                DropdownMenuItem(
                    text = { Text("Week $weekNumber") },
                    onClick = {
                        budgetViewModel.changeCurrentWeek(weekNumber)
                        isExpanded = false
                    }
                )
            }
        }
    }
}
