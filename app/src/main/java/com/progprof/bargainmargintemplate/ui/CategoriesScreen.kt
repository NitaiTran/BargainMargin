package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun CategoriesScreen(navController: NavController,
                     categoryList: List<Category>,
                     onAddCategory: (String, Double) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Categories Screen")

        DrawCategoryInput(onAddCategory)

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back to Home")
        }
    }
}

@Composable
fun DrawCategoryInput(onAddCategory: (String, Double) -> Unit) {
    var categoryName by remember { mutableStateOf("") }
    var categoryBudget by remember {mutableStateOf("")}
    var nameErrorMessage by remember {mutableStateOf<String?>(null)}
    var budgetErrorMessage by remember {mutableStateOf<String?>(null)}

    TextField(
        value = categoryName,
        onValueChange = {
            categoryName = it
            budgetErrorMessage = null
        },
        label = {Text("Enter category name")},
        isError = nameErrorMessage != null
    )

    TextField(
        value = categoryBudget,
        onValueChange = {
            categoryBudget = it
            budgetErrorMessage = null
        },
        label = {Text("Enter budget amount")},
        isError = budgetErrorMessage != null
    )

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

    Button(onClick = {
        if (categoryName.isBlank()) {
            nameErrorMessage = "Category name cannot be empty"
        }
        else if (categoryBudget.toDoubleOrNull() == null) {
            budgetErrorMessage = "Budget amount must be a valid number"
        }
        else {
            onAddCategory(categoryName, categoryBudget.toDouble())
            nameErrorMessage = null
            budgetErrorMessage = null
        }
    }) {
        Text("Add Category")
    }
}