package com.progprof.bargainmargintemplate


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost //if red, do a gradle sync
import androidx.navigation.compose.composable
import com.progprof.bargainmargintemplate.ui.* //imports everything from the ui package
import com.progprof.bargainmargintemplate.ui.theme.AppTheme




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background)
                {
                    //Entry point for the App's navigation
                    BargainMarginApp()
                }
            }
        }
    }
}

@Composable
fun BargainMarginApp() {
    // Create the NavController, which manages the back stack and navigation
    val navController = androidx.navigation.compose.rememberNavController()
    // Share one ViewModel instance across both screens, so variables are shared
    val budgetViewModel: BudgetViewModel = viewModel()

    NavHost(
        navController = navController, // The NavController
        startDestination = ScreenController.Screen.MainBudgetEntry.name // The first screen to show
    ){
        //Define Home screen
        composable(route = ScreenController.Screen.Home.name){
            HomeScreen(
                navController = navController,
                budgetViewModel = budgetViewModel
            )
        }

        //Define the Main Budget Screen
        composable(route = ScreenController.Screen.MainBudgetEntry.name){
            BudgetScreen(
                initialScreenViewModel = budgetViewModel,
                onNextButtonClicked = {
                    // 1. Set the budget in the ViewModel
                    budgetViewModel.setInitialBudget()
                    // 2. Navigate to Home screen
                    navController.navigate(ScreenController.Screen.SplitMainBudget.name)
                }
            )
        }

        // Define the Split Main Budget Screen
        composable(route = ScreenController.Screen.SplitMainBudget.name) {
           SplitBudgetScreen(
               budgetViewModel = budgetViewModel,
               onNextButtonClicked = {
                   // 1. Set the budget in the ViewModel
                   budgetViewModel.setInitialBudget()
                   budgetViewModel.setCategories()
                   // 2. Navigate to the next screen
                   navController.navigate(ScreenController.Screen.Home.name)
               }
           )
        }


        // Define Expense Tracking screen
        composable("expenseTracker") {
            ExpensesScreen(
                budgetViewModel = budgetViewModel
            )
        }



        // Define Analytics screen
        composable(route = ScreenController.Screen.Analytics.name) {
            AnalyticsScreen(navController = navController, budgetViewModel)
        }

        // Define Categories screen
        composable(route = ScreenController.Screen.Categories.name) {
            CategoriesScreen(navController = navController)
        }

        // Define Settings screen
        composable(route = ScreenController.Screen.Settings.name) {
            SettingsScreen(navController = navController)
        }
    }

}

