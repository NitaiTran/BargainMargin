package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalScreen(
    budgetViewModel: BudgetViewModel,
    navController: NavController
) {
    val uiState by budgetViewModel.uiState.collectAsState()

    var monthlyGoalInput by remember { mutableStateOf("") }
    var weeklyGoalInput by remember { mutableStateOf("") }

    LaunchedEffect(uiState.currentMonthGoal) {
        monthlyGoalInput = uiState.currentMonthGoal.toString()
    }
    LaunchedEffect(uiState.currentWeekGoal) {
        weeklyGoalInput = uiState.currentWeekGoal.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Set Your Goals") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Monthly Goal Input ---
            OutlinedTextField(
                value = monthlyGoalInput,
                onValueChange = { monthlyGoalInput = it },
                label = { Text("Monthly Savings Goal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    budgetViewModel.updateMonthGoal(monthlyGoalInput.toDoubleOrNull() ?: 0.0)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Monthly Goal")
            }

            Spacer(Modifier.height(32.dp))

            // --- Weekly Goal Input ---
            Text("Current Week: ${uiState.currentWeekNumber}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = weeklyGoalInput,
                onValueChange = { weeklyGoalInput = it },
                label = { Text("Savings Goal for Week ${uiState.currentWeekNumber}") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    budgetViewModel.updateWeeklyGoal(
                        weekNumber = uiState.currentWeekNumber,
                        newGoal = weeklyGoalInput.toDoubleOrNull() ?: 0.0
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Weekly Goal")
            }
        }
    }
}
