package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- THIS IS THE FIX ---
// The signature is now clean. It only asks for the data it needs and the functions to call.
@Composable
fun BudgetScreen(
    budgetString: String,
    onBudgetStringChange: (String) -> Unit,
    onNextButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        AppTitle(Modifier)

        // The LogInitialRemainingBudget composable is simple enough that we can just use
        // the NumberField directly here.
        NumberField(
            labelText = "What is your budget?",
            textInput = budgetString,
            onValueChange = onBudgetStringChange, // Pass the correct event handler
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )

        CalculateButton(
            onClick = onNextButtonClicked, // This was already correct
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

// This is a helpful, reusable composable. Let's keep it.
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
