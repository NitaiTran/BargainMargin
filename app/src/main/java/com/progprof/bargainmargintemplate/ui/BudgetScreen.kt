package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.error
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BudgetScreen(
    budgetViewModel: BudgetViewModel,
    onNextButtonClicked: () -> Unit
) {
    val uiState by budgetViewModel.uiState.collectAsState()
    val month = uiState.monthWithWeeks?.month

    var textInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(month) {
        if (month != null && month.totalBudget > 0) {
            // Use a clean format to avoid scientific notation like "1.0E-4"
            textInput = month.totalBudget.toBigDecimal().toPlainString()
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        AppTitle(Modifier)

        NumberField(
            labelText = "What is your total budget for this month?",
            textInput = textInput,
            onValueChange = {
                textInput = it
                errorMessage = null // Clear error message on new input
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        CalculateButton(
            onClick = {
                val newTotal = textInput.toDoubleOrNull()
                if (newTotal == null || newTotal <= 0) {
                    errorMessage = "Please enter a valid, positive budget amount."
                } else {
                    budgetViewModel.createNewMonthBudget(newTotal)
                    onNextButtonClicked()
                }
            },
            modifier = Modifier
        )
    }
}

@Composable
fun AppTitle(modifier: Modifier = Modifier) {
    Text(
        text = "BargainMargin",
        fontSize = 38.sp,
        modifier = modifier.padding(bottom = 16.dp, start = 70.dp)
    )
}

@Composable
fun CalculateButton(
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

@Composable
fun NumberField(
    labelText: String,
    textInput: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = textInput,
        onValueChange = onValueChange,
        label = { Text(labelText) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        modifier = modifier
    )
}