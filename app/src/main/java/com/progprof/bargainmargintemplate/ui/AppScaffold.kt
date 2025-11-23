package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DateRange
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
import java.text.DateFormatSymbols

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
    var showOptionsMenu by remember { mutableStateOf(false) }
    var showWeekDialog by remember { mutableStateOf(false) }
    var showMonthMenu by remember { mutableStateOf(false) }
    val uiState by budgetViewModel.uiState.collectAsState()

    val monthSymbols = DateFormatSymbols().months

    TopAppBar(
        title = { Text("Bargain Margin") },
        actions = {
            Box {
                IconButton(onClick = { showMonthMenu = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select Month")
                }
                DropdownMenu(
                    expanded = showMonthMenu,
                    onDismissRequest = { showMonthMenu = false }
                ) {
                    uiState.allMonths.forEach { month ->
                        DropdownMenuItem(
                            text = { Text("${monthSymbols[month.month]} ${month.year}") },
                            onClick = {
                                budgetViewModel.changeSelectedMonth(month.year, month.month)
                                showMonthMenu = false
                            }
                        )
                    }
                }
            }


            Box {
                IconButton(onClick = { showOptionsMenu = !showOptionsMenu }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = showOptionsMenu,
                    onDismissRequest = { showOptionsMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Set Goals") },
                        onClick = {
                            navController.navigate("goals") // New route name
                            showOptionsMenu = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Edit Monthly Budget") },
                        onClick = {
                            navController.navigate(ScreenController.Screen.MainBudgetEntry.name)
                            showOptionsMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Edit Weekly Budgets") },
                        onClick = {
                            navController.navigate(ScreenController.Screen.WeeklyBudgetEntry.name)
                            showOptionsMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Change Current Week") },
                        onClick = {
                            showWeekDialog = true
                            showOptionsMenu = false
                        }
                    )
                }
            }
            OptionsDialog(
                budgetViewModel = budgetViewModel,
                showDialog = showWeekDialog,
                onDismissRequest = { showWeekDialog = false },
                onOptionSelected = { /* option handled in dialog */ }
            )
        }
    )
}

@Composable
fun AppBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, ScreenController.Screen.Home.name),
        BottomNavItem("Expenses", Icons.Default.TrackChanges, ScreenController.Screen.ExpenseTracker.name),
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
                navController.navigate(ScreenController.Screen.WeeklyBudgetEntry.name) {
                    popUpTo(ScreenController.Screen.MainBudgetEntry.name) { inclusive = true }
                }
            })
        }


        composable(ScreenController.Screen.ExpenseTracker.name) {
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

        composable("goals") {
            GoalScreen(budgetViewModel = budgetViewModel, navController = navController)
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
