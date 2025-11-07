package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// --- THIS IS THE FIX ---
// The signature is now clean. It only asks for the data it needs and the functions to call.
@Composable
fun WeeklyBudgetScreen(
    navController: NavHostController,
    budgetViewModel: BudgetViewModel,
    //budgetString: String,
    //onBudgetStringChange: (String) -> Unit,
    onNextButtonClicked: () -> Unit
) {
    val budgetState by budgetViewModel.budgetRepo.collectAsState()
    var week1DefaultInput by remember { mutableStateOf<String>(budgetState?.week1TotalBudget.toString())}
    var week2DefaultInput by remember { mutableStateOf<String>(budgetState?.week2TotalBudget.toString())}
    var week3DefaultInput by remember { mutableStateOf<String>(budgetState?.week3TotalBudget.toString())}
    var week4DefaultInput by remember { mutableStateOf<String>(budgetState?.week4TotalBudget.toString())}
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        AppTitle(Modifier)

        // The LogInitialRemainingBudget composable is simple enough that we can just use
        // the NumberField directly here.
        Text("Edit Week 1 TOTAL Budget: ")
        TextField(
            value = week1DefaultInput,
            onValueChange = { week1DefaultInput = it },
            label = { Text("Week 1 Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Edit Week 2 TOTAL Budget: ")
        TextField(
            value = week2DefaultInput,
            onValueChange = { week2DefaultInput = it },
            label = { Text("Week 2 Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Edit Week 3 TOTAL Budget: ")
        TextField(
            value = week3DefaultInput,
            onValueChange = { week3DefaultInput = it },
            label = { Text("Week 3 Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Edit Week 4 TOTAL Budget: ")
        TextField(
            value = week4DefaultInput,
            onValueChange = { week4DefaultInput = it },
            label = { Text("Week 4 Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(16.dp))



        WeeklyCalculateButton(
            navController= navController,
            onClick = onNextButtonClicked, // This was already correct
            modifier = Modifier,
            budgetViewModel = budgetViewModel,
            week1Input = week1DefaultInput,
            week2Input = week2DefaultInput,
            week3Input = week3DefaultInput,
            week4Input = week4DefaultInput
        )
    }
}

@Composable
fun WeeklyCalculateButton(
    navController: NavHostController,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    budgetViewModel: BudgetViewModel,
    week1Input: String,
    week2Input: String,
    week3Input: String,
    week4Input: String
) {
    val budgetState by budgetViewModel.budgetRepo.collectAsState()
    var myErrorMessage by remember { mutableStateOf<String?>(null) }
    if (myErrorMessage != null) {
        Text(myErrorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
    }
    Button(
        onClick = {
            val newWeek1 = week1Input.toDoubleOrNull()
            val newWeek2 = week2Input.toDoubleOrNull()
            val newWeek3 = week3Input.toDoubleOrNull()
            val newWeek4 = week4Input.toDoubleOrNull()


            when {
                newWeek1 == null || newWeek1 <= 0 -> myErrorMessage = "Please enter a valid Week 1 amount."
                newWeek2 == null || newWeek2 <= 0 -> myErrorMessage = "Please enter a valid Week 2 amount."
                newWeek3 == null || newWeek3 <= 0 -> myErrorMessage = "Please enter a valid Week 3 amount."
                newWeek4 == null || newWeek4 <= 0 -> myErrorMessage = "Please enter a valid Week 4 amount."
                (newWeek1 + newWeek2 + newWeek3 + newWeek4) > (budgetState?.totalRemainingBudget ?: 0.0) -> myErrorMessage = "Weekly budgets go over monthly budget."
                else -> {
                    val newMonthlyTotal = newWeek1 + newWeek2 + newWeek3 + newWeek4
                    budgetViewModel.setBudgetLimit(newMonthlyTotal)
                    budgetViewModel.alterWeeklyBudgets(newWeek1,newWeek2,newWeek3,newWeek4)

                    myErrorMessage = null
                    //budgetViewModel.changeBudgetLimit()
                    navController.navigate(ScreenController.Screen.Home.name)
                }
            }

        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Change!")
    }
}

// This is a helpful, reusable composable. Let's keep it.

