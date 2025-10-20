package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SplitBudgetScreen (
    modifier: Modifier = Modifier,
    budgetViewModel: BudgetViewModel = viewModel()
) {
    Column ( modifier = modifier.padding(16.dp) ){
        Text(
            text = "Split your budget",
            fontSize = 24.sp,
            modifier = modifier.padding(bottom = 16.dp)
        )

        // Display the total budget entered on the previous screen
        Text(
            text = "Total Budget: $${budgetViewModel.monthlyRemainingBudget}",
            fontSize = 18.sp,
            modifier = modifier.padding(bottom = 24.dp)
        )

        // Use the existing NumberField to ask for the number of categories
        NumberField(
            labelText = "How many categories to split this into?",
            textInput = budgetViewModel.categories,
            onValueChange = { budgetViewModel.categories = it },
            modifier = modifier.padding(bottom = 16.dp)
        )

    }

}