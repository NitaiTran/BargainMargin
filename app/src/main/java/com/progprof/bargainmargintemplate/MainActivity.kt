package com.progprof.bargainmargintemplate


import android.content.Context
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


// ... inside your Activity or Fragment




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
                    AppScaffold()
                }
            }
        }
    }


}
