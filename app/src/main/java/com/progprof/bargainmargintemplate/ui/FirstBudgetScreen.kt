package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.viewmodel.compose.viewModel

import com.progprof.bargainmargintemplate.ui.theme.AppTheme

@Composable
fun FirstBudgetScreen(
    onNextButtonClicked: () -> Unit, // Add a parameter to handle the navigation event,Sprint 2, Jose
    modifier: Modifier = Modifier,
    initialScreenViewModel: BudgetViewModel = viewModel()
) {
    Column (
        modifier = modifier.padding(10.dp)
    ) {
        AppTitle(modifier)
        LogInitialBudget(
            totalBudget = initialScreenViewModel.totalBudget,
            onValueChange = { initialScreenViewModel.totalBudget = it },
            modifier = modifier
        )
        CalculateButton(
            onClick = onNextButtonClicked, // Call the navigation event handler,Sprint 2, Jose
            modifier = modifier
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
fun LogInitialBudget(
    totalBudget: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NumberField(
        labelText = "What is your initial budget?",
        textInput = totalBudget,
        onValueChange = { onValueChange(it) },
        modifier = modifier.padding(bottom = 16.dp).fillMaxWidth()
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
        label = {Text(labelText)},
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        modifier = modifier

    )
}

@Composable
fun RadioGroup(
    labelText: String,
    radioOptions: List<String>,
    selectedOption: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelectedOption: (String) -> Boolean = { selectedOption == it }
    Column {
        Text(labelText)
        radioOptions.forEach { option ->
            Row (
                modifier = modifier
                    .selectable(
                        selected = isSelectedOption(option),
                        onClick = {onSelected(option)},
                        role = Role.RadioButton
                    )
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = isSelectedOption(option),
                    onClick = null,
                    modifier = modifier.padding(end = 8.dp)
                )
                Text(
                    text = option,
                    modifier = modifier.fillMaxWidth()
                )
            }

        }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    AppTheme {
//        FirstBudgetScreen()
//    }
//}
