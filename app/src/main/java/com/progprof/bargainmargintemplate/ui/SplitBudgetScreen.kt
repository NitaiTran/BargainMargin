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

@Composable
fun SplitBudgetScreen(
    budgetViewModel: BudgetViewModel,
    categoriesString: String,
    onCategoriesChange: (String) -> Unit,
    onNextButtonClicked: () -> Unit,
) {
    val budget by budgetViewModel.budgetState.collectAsState()

    val monthlyRemainingBudget = budget.monthlyRemainingBudget
    val totalBudget = budget.totalBudget

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Split your budget",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(

            text = "Total Budget: $${String.format("%.2f", monthlyRemainingBudget)} / $${String.format("%.2f", totalBudget)}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

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
