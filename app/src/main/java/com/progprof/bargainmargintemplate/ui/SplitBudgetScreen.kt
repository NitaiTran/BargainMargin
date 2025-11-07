package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SplitBudgetScreen (
    // The screen should not create its own ViewModel. It should receive the values it needs.
    // This makes your UI more predictable and easier to test.
    budgetViewModel: BudgetViewModel,
    categoriesString: String,
    onCategoriesChange: (String) -> Unit,
    onNextButtonClicked: () -> Unit,
) {
    val budgetState by budgetViewModel.budgetRepo.collectAsState()
    val monthlyRemainingBudget = budgetState?.monthlyRemainingBudget
    val totalRemainingBudget = budgetState?.totalRemainingBudget
    Column ( modifier = Modifier.padding(16.dp) ){
        Text(
            text = "Split your budget",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display the budget values passed into the screen
        Text(
            // Use String.format for clean currency display
            text = "Total Budget: $${String.format("%.2f", monthlyRemainingBudget)}/${String.format("%.2f", totalRemainingBudget)}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Using a standard Material 3 OutlinedTextField instead of the undefined NumberField
        OutlinedTextField(
            value = categoriesString,
            onValueChange = onCategoriesChange,
            label = { Text("How many categories to split this into?") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        GoToMainScreenButton(
            onClick = onNextButtonClicked,
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
