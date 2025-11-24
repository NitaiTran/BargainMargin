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
import androidx.navigation.NavController
import com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity
import com.progprof.bargainmargintemplate.data.local.entities.CategorySpendingHistoryEntity
import kotlinx.coroutines.launch

@Composable
fun CategoriesScreen(
    budgetViewModel: BudgetViewModel,
    navController: NavController
) {
    val allCategories by budgetViewModel.categories.collectAsState()
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    var snapshotMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val snapshots by budgetViewModel.currentMonthSnapshots.collectAsState()

    val currentYear = remember { java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) }
    val currentMonth = remember { java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) }

    LaunchedEffect(true) {
        budgetViewModel.loadCurrentMonthSnapshots()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Snapshot Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Monthly Snapshot",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                snapshotMessage = null
                                scope.launch {
                                    budgetViewModel.generateMonthlyCategorySnapshots(currentYear, currentMonth)
                                    snapshotMessage = "Snapshot saved for ${currentMonth + 1}/$currentYear"
                                    budgetViewModel.loadCurrentMonthSnapshots()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Monthly Snapshot")
                        }

                        snapshotMessage?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        if (snapshots.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(12.dp))

                            snapshots.forEach { snapshot ->
                                val category = allCategories.find { it.id == snapshot.categoryId }
                                if (category != null) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = category.categoryName,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "$${"%.2f".format(snapshot.amountSpent)} / $${"%.2f".format(snapshot.totalBudgetAtSnapshot)}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Add Category Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Add Category",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        DrawCategoryInput(onAddCategory = { name, budget ->
                            budgetViewModel.addCategory(name, budget)
                        })
                    }
                }
            }

            // Categories List Header
            item {
                Text(
                    "Your Categories",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Category Cards
            if (allCategories.isEmpty()) {
                item {
                    Text(
                        "No categories yet. Add one above!",
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(allCategories, key = { it.id }) { category ->
                    DrawCategoryCard(
                        category = category,
                        onRemoveCategory = {
                            budgetViewModel.removeCategory(
                                CategoryEntity(it.id, it.categoryName, it.totalBudget, it.budgetRemaining)
                            )
                        },
                        onEditCategory = { editingCategory = it }
                    )
                }
            }
        }

        // Edit Dialog
        if (editingCategory != null) {
            EditCategory(
                category = editingCategory!!,
                onDismiss = { editingCategory = null },
                onSave = { updatedCategory ->
                    budgetViewModel.updateCategory(
                        CategoryEntity(editingCategory!!.id, editingCategory!!.categoryName, editingCategory!!.totalBudget, editingCategory!!.budgetRemaining),
                        CategoryEntity(updatedCategory.id, updatedCategory.categoryName, updatedCategory.totalBudget, updatedCategory.budgetRemaining)
                    )
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
        label = { Text("Category name") },
        isError = nameErrorMessage != null
    )

    if (nameErrorMessage != null) {
        Text(
            text = nameErrorMessage!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = categoryBudget,
        onValueChange = {
            categoryBudget = it
            budgetErrorMessage = null
        },
        label = { Text("Budget amount") },
        isError = budgetErrorMessage != null
    )

    if (budgetErrorMessage != null) {
        Text(
            text = budgetErrorMessage!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

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
        }
    ) {
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    category.categoryName,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Budget: $${"%.2f".format(category.totalBudget)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Remaining: $${"%.2f".format(category.budgetRemaining)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (category.budgetRemaining < 0)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
            }

            Row {
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
                    Spacer(Modifier.height(8.dp))
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