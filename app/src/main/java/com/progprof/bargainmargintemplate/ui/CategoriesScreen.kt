package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoriesScreen(
    budgetViewModel: BudgetViewModel
) {

    val allCategories by budgetViewModel.categories.collectAsState()
    var editingCategory by remember { mutableStateOf<Category?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        DrawCategoryInput(onAddCategory = { name, budget ->
            budgetViewModel.addCategory(name, budget)
        })
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) { // Added fillMaxSize here
            if (allCategories.isEmpty()) {
                item {
                    Text("No categories yet.", modifier = Modifier.padding(top = 8.dp))
                }
            } else {
                items(allCategories, key = { it.id }) { category ->
                    DrawCategoryCard(
                        category,
                        onRemoveCategory = { budgetViewModel.removeCategory(it) },
                        onEditCategory = { editingCategory = it }
                    )
                }
            }
        }

        if (editingCategory != null) {
            EditCategory(
                category = editingCategory!!,
                onDismiss = { editingCategory = null },
                onSave = { updatedCategory ->
                    budgetViewModel.updateCategory(editingCategory!!, updatedCategory)
                    editingCategory = null
                }
            )
        }
    }
}

@Composable
fun DrawCategoryInput(onAddCategory: (String, Double) -> Unit) {
    var categoryName by remember { mutableStateOf("") }
    var categoryBudget by remember { mutableStateOf("") }
    var nameErrorMessage by remember { mutableStateOf<String?>(null) }
    var budgetErrorMessage by remember { mutableStateOf<String?>(null) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = categoryName,
        onValueChange = {
            categoryName = it
            nameErrorMessage = null
        },
        label = { Text("Enter category name") },
        isError = nameErrorMessage != null
    )

    Spacer(modifier = Modifier.height(8.dp))

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = categoryBudget,
        onValueChange = {
            categoryBudget = it
            budgetErrorMessage = null
        },
        label = { Text("Enter budget amount") },
        isError = budgetErrorMessage != null
    )

    Spacer(modifier = Modifier.height(8.dp))

    if (nameErrorMessage != null) {
        Text(
            text = nameErrorMessage!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }

    if (budgetErrorMessage != null) {
        Text(
            text = budgetErrorMessage!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val parsedBudget = categoryBudget.toDoubleOrNull()
            val isNameInvalid = categoryName.isBlank()
            val isBudgetInvalid = parsedBudget == null || parsedBudget <= 0

            if (isNameInvalid) {
                nameErrorMessage = "Category name cannot be empty"
            }
            if (isBudgetInvalid) {
                budgetErrorMessage = "Budget must be a valid, positive number"
            }

            if (!isNameInvalid && !isBudgetInvalid) {
                onAddCategory(categoryName, parsedBudget!!)
                nameErrorMessage = null
                budgetErrorMessage = null
                categoryName = ""
                categoryBudget = ""
            }
        }) {
        Text("Add Category")
    }
}

@Composable
fun DrawCategoryCard(
    category: Category,
    onRemoveCategory: (Category) -> Unit,
    onEditCategory: (Category) -> Unit
) {
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
                Text(category.categoryName, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Budget: $${"%.2f".format(category.totalBudget)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Row { // Use a Row for the buttons
                TextButton(onClick = { onEditCategory(category) }) {
                    Text("Edit")
                }

                TextButton(onClick = { onRemoveCategory(category) }) {
                    Text("Remove", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun EditCategory(category: Category, onDismiss: () -> Unit, onSave: (Category) -> Unit) {
    var name by remember { mutableStateOf(category.categoryName) }
    var totalBudget by remember { mutableStateOf(category.totalBudget.toString()) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Category") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    isError = error?.contains("Name") == true
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = totalBudget,
                    onValueChange = { totalBudget = it },
                    label = { Text("Budget") },
                    isError = error?.contains("budget") == true
                )

                if (error != null) {
                    Text(text = error!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val parsedBudget = totalBudget.toDoubleOrNull()
                if (name.isBlank()) {
                    error = "Name cannot be empty"
                } else if (parsedBudget == null || parsedBudget <= 0) {
                    error = "Invalid budget amount"
                } else {
                    onSave(category.copy(categoryName = name, totalBudget = parsedBudget, budgetRemaining = parsedBudget))
                    onDismiss()
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}