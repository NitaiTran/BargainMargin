
package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BudgetScreen(
    budgetViewModel: BudgetViewModel,
    onNextButtonClicked: () -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    val budget by budgetViewModel.budgetState.collectAsState()

    LaunchedEffect(budget) {
        if (budget.totalBudget > 0) {
            textInput = budget.totalBudget.toString()
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        AppTitle(Modifier)

        NumberField(
            labelText = "What is your budget?",
            textInput = textInput,
            onValueChange = {
                textInput = it
                budgetViewModel.onTotalBudgetChanged(it)
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )

        CalculateButton(
            onClick = onNextButtonClicked,
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
