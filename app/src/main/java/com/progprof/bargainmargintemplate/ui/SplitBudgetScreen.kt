package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SplitBudgetScreen (
    budgetViewModel: BudgetViewModel = viewModel(),
    onNextButtonClicked: () -> Unit, // Add a parameter to handle the navigation event,Sprint 2, Jose

) {
    Column ( modifier = Modifier.padding(16.dp) ){
        Text(
            text = "Split your budget",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display the total budget entered on the previous screen
        Text(
            text = "Total Budget: $${budgetViewModel.monthlyRemainingBudget}/${budgetViewModel.totalRemainingBudget}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Use the existing NumberField to ask for the number of categories
        NumberField(
            labelText = "How many categories to split this into?",
            textInput = budgetViewModel.categories,
            onValueChange = { budgetViewModel.categories = it },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        GoToMainScreenButton(
            onClick = onNextButtonClicked, // Call the navigation event handler,Sprint 2, Jose
            modifier = Modifier
        )

    }

}

@Composable
fun GoToMainScreenButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Submit!")
    }
}

