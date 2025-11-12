package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val budgetViewModel: BudgetViewModel = viewModel()

    Scaffold(
        topBar = { AppTopBar(navController = navController, budgetViewModel = budgetViewModel) },
        bottomBar = { AppBottomBar(navController = navController) }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            budgetViewModel = budgetViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController, budgetViewModel: BudgetViewModel) {
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Bargain Margin") },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Edit Monthly Budget") },
                    onClick = {
                        navController.navigate(ScreenController.Screen.MainBudgetEntry.name)
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Edit Weekly Budgets") },
                    onClick = {
                        navController.navigate(ScreenController.Screen.WeeklyBudgetEntry.name)
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Change Current Week") },
                    onClick = {
                        showDialog = true
                        // The menu should close when the dialog opens
                        showMenu = false
                    }
                )
            }
            OptionsDialog(
                budgetViewModel = budgetViewModel,
                showDialog = showDialog,
                onDismissRequest = { showDialog = false },
                onOptionSelected = { /* option handled in dialog */ }
            )
        }
    )
}

@Composable
fun AppBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, ScreenController.Screen.Home.name),
        BottomNavItem("Expenses", Icons.Default.TrackChanges, "expenseTracker"),
        BottomNavItem("Categories", Icons.Default.Category, ScreenController.Screen.Categories.name),
        BottomNavItem("Analytics", Icons.Default.Analytics, ScreenController.Screen.Analytics.name)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    budgetViewModel: BudgetViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ScreenController.Screen.Home.name,
        modifier = modifier
    ) {
        composable(ScreenController.Screen.Home.name) {
            HomeScreen(budgetViewModel = budgetViewModel, navController = navController)
        }

        composable(ScreenController.Screen.MainBudgetEntry.name) {
            BudgetScreen(budgetViewModel = budgetViewModel, onNextButtonClicked = {
                navController.navigate(ScreenController.Screen.Home.name) {
                    popUpTo(ScreenController.Screen.Home.name) { inclusive = true }
                }
            })
        }

        composable("expenseTracker") {
            ExpensesScreen(budgetViewModel = budgetViewModel, navController = navController)
        }

        composable(ScreenController.Screen.WeeklyBudgetEntry.name) {
            WeeklyBudgetScreen(navController = navController, budgetViewModel = budgetViewModel)
        }

        composable(ScreenController.Screen.Categories.name) {
            CategoriesScreen(budgetViewModel = budgetViewModel)
        }

        composable(ScreenController.Screen.Analytics.name) {
            AnalyticsScreen(budgetViewModel = budgetViewModel)
        }

    }
}


@Composable
fun OptionsDialog(
    budgetViewModel: BudgetViewModel,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onOptionSelected: (Int) -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text("Change Current Week") },
            text = {
                Column {
                    (1..4).forEach { week ->
                        TextButton(onClick = {
                            onOptionSelected(week)
                            budgetViewModel.changeCurrentWeek(week)
                            onDismissRequest()
                        }) {
                            Text("Week $week")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
            }
        )
    }
}
