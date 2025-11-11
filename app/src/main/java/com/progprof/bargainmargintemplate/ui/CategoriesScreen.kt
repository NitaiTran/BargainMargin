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
    val budget by budgetViewModel.budgetState.collectAsState()
    val allCategories by budgetViewModel.categories.collectAsState()
    var editingCategory by remember { mutableStateOf<Category?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        DrawCategoryInput(onAddCategory = { name, budget, remaining ->
            budgetViewModel.addCategory(name, budget)
        })
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
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
                editingCategory!!,
                onDismiss = { editingCategory = null },
                onSave = { updated ->
                    budgetViewModel.updateCategory(editingCategory!!, updated)
                    editingCategory = null
                }
            )
        }
    }
}

@Composable
fun DrawCategoryInput(onAddCategory: (String, Double, Double) -> Unit) {
    var categoryName by remember { mutableStateOf("") }
    var categoryBudget by remember { mutableStateOf("") }
    var nameErrorMessage by remember { mutableStateOf<String?>(null) }
    var budgetErrorMessage by remember { mutableStateOf<String?>(null) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = categoryName,
        onValueChange = {
            categoryName = it
            budgetErrorMessage = null
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
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall
        )
    }

    if (budgetErrorMessage != null) {
        Text(
            text = budgetErrorMessage!!,
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall
        )
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (categoryName.isBlank()) {
                nameErrorMessage = "Category name cannot be empty"
            } else if (categoryBudget.toDoubleOrNull() == null || categoryBudget.toDoubleOrNull()!! <= 0) {
                budgetErrorMessage = "Budget amount must be a valid number"
            } else {
                onAddCategory(categoryName, categoryBudget.toDouble(), categoryBudget.toDouble())
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
            Column {
                Text(category.categoryName, style = MaterialTheme.typography.bodyLarge)
                Text(text = category.totalBudget.toString(), style = MaterialTheme.typography.bodyLarge)
            }

            Column {
                TextButton(onClick = { onEditCategory(category) }) {
                    Text("Edit")
                }

                TextButton(onClick = { onRemoveCategory(category) }) {
                    Text("Remove", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun EditCategory(category: Category, onDismiss: () -> Unit, onSave: (Category) -> Unit) {
    var name by remember { mutableStateOf(category.categoryName) }
    var totalBudget by remember { mutableStateOf("") }
    var remainingBudget by remember { mutableStateOf(category.budgetRemaining) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Category") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = totalBudget,
                    onValueChange = { totalBudget = it },
                    label = { Text("Budget") }
                )

                if (error != null) {
                    Text(text = error!!, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val parsedBudget = totalBudget.toDoubleOrNull()
                if (name.isBlank()) {
                    error = "Name cannot be empty"
                } else if (parsedBudget == null || parsedBudget <= 0) {
                    error = "Invalid budget"
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
