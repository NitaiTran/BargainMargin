package com.progprof.bargainmargintemplate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Data class to define our bottom navigation items
data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

/**
 * This is the main entry point for the new UI. It sets up the Scaffold,
 * Top App Bar, Bottom Navigation, and the NavHost for screen content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    val navController = rememberNavController() // this returns a NavHostController
    val budgetViewModel: BudgetViewModel = viewModel()

    Scaffold(
        topBar = { AppTopBar(navController = navController) },
        bottomBar = { AppBottomBar(navController = navController) }
    ) { innerPadding ->
        // The NavHost contains all the screens.
        // It's given padding to appear below the top bar and above the bottom bar.
        AppNavHost(
            navController = navController,
            budgetViewModel = budgetViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController) {
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val budgetViewModel: BudgetViewModel = viewModel()
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Change Week") },
            text = { Text("Choose Current Week.") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    // Perform confirmation action here
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Dismiss")
                }
            }
        )
    }

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
                        // Navigate to the budget entry screen when clicked
                        navController.navigate(ScreenController.Screen.MainBudgetEntry.name)
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Edit Weekly Budgets") },
                    onClick = {
                        // Navigate to the budget entry screen when clicked
                        navController.navigate(ScreenController.Screen.WeeklyBudgetEntry.name)
                        showMenu = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Change Current Week") },
                    onClick = {
                        showDialog = true;
                    }
                )

                OptionsDialog(
                    budgetViewModel = budgetViewModel,
                    showDialog = showDialog,
                    onDismissRequest = {
                        showDialog = false
                        showMenu = false },
                    onOptionSelected = { option ->
                        // Handle the selected option here
                        println("Selected Option: $option")
                    }
                )
            }
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
                        // Pop up to the start destination to avoid building up a large back stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * This NavHost is very similar to old one, but it now lives inside the Scaffold.
 * It defines all the possible navigation destinations in your app.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    budgetViewModel: BudgetViewModel,
    modifier: Modifier = Modifier
) {
    var startingScreen = ScreenController.Screen.Home.name
    val budgetState by budgetViewModel.budgetRepo.collectAsState()
    budgetState?.totalBudget?.let {
    if (it <=0)
    {
        startingScreen = ScreenController.Screen.MainBudgetEntry.name
    } else
    {
        startingScreen = ScreenController.Screen.Home.name
    }
    }
    NavHost(
        navController = navController,
        startDestination = startingScreen,
        modifier = modifier
    ) {
        // --- CHANGE: All screens now use `collectAsState` ---

        composable(ScreenController.Screen.Home.name) {

            // 2. Pass the raw data to the clean HomeScreen. Note that NavController is no longer passed.
            HomeScreen(
                budgetViewModel = budgetViewModel
            )
        }
        composable(ScreenController.Screen.MainBudgetEntry.name) {
            val budgetString by budgetViewModel.totalBudget.collectAsState()
            BudgetScreen(
                budgetString = budgetString,
                onBudgetStringChange = { newString -> budgetViewModel.onTotalBudgetChanged(newString) },
                onNextButtonClicked = {
                    budgetViewModel.changeBudgetLimit()
                    navController.navigate(ScreenController.Screen.SplitMainBudget.name)
                }
            )
        }
        composable(ScreenController.Screen.WeeklyBudgetEntry.name) {
            //val budgetString by budgetViewModel.totalBudget.collectAsState()
            WeeklyBudgetScreen(
                navController = navController,
                budgetViewModel = budgetViewModel,
                //budgetString = budgetString,
                //onBudgetStringChange = { newString -> budgetViewModel.onTotalBudgetChanged(newString) },
                onNextButtonClicked = {

                }
            )
        }
        composable(ScreenController.Screen.SplitMainBudget.name) {
            // 1. Collect all the states the screen needs from the ViewModel.
            val categoriesString by budgetViewModel.categories.collectAsState()
            val monthlyBudget by budgetViewModel.monthlyRemainingBudget.collectAsState()
            val totalBudget by budgetViewModel.totalRemainingBudget.collectAsState()

            // 2. Pass those states and the correct lambdas to the screen.
            SplitBudgetScreen(
                budgetViewModel = budgetViewModel,
                categoriesString = categoriesString,
                onCategoriesChange = { newString -> budgetViewModel.onCategoriesChanged(newString) },
                onNextButtonClicked = {
                    budgetViewModel.settingUpVariables()
                    navController.navigate(ScreenController.Screen.Home.name) {
                        // Clear the setup screens from the back stack so the user can't go "back" to them
                        popUpTo(ScreenController.Screen.MainBudgetEntry.name) { inclusive = true }
                    }
                }
            )
        }
        composable("expenseTracker") {
            // NOTE: ExpensesScreen will also need to be refactored to use collectAsState
            ExpensesScreen(budgetViewModel = budgetViewModel, navController = navController)
        }
        composable(ScreenController.Screen.Analytics.name) {
            // NOTE: AnalyticsScreen will also need to be refactored
            AnalyticsScreen(navController = navController, budgetViewModel = budgetViewModel)
        }
        composable(ScreenController.Screen.Categories.name) {
            // NOTE: CategoriesScreen will also need to be refactored
            CategoriesScreen(navController = navController)
        }
    }
}

@Composable
fun OptionsDialog(
    budgetViewModel: BudgetViewModel,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onOptionSelected: (Int) -> Unit // Callback for selected option
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text("Change Current Week") },
            text = {
                Column {
                    TextButton(onClick = {
                        onOptionSelected(1); onDismissRequest()
                        budgetViewModel.changeCurrentWeek(1)
                    }) {
                        Text("Week 1")
                    }
                    TextButton(onClick = {
                        onOptionSelected(2); onDismissRequest()
                        budgetViewModel.changeCurrentWeek(2)
                    }) {
                        Text("Week 2")
                    }
                    TextButton(onClick = {
                        onOptionSelected(3); onDismissRequest()
                        budgetViewModel.changeCurrentWeek(3)
                    }) {
                        Text("Week 3")
                    }
                    TextButton(onClick = {
                        onOptionSelected(4); onDismissRequest()
                        budgetViewModel.changeCurrentWeek(4)
                    }) {
                        Text("Week 4")
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

