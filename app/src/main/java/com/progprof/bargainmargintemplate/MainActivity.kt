package com.progprof.bargainmargintemplate

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.progprof.bargainmargintemplate.databinding.ActivityMainBinding
import android.widget.EditText
import android.view.inputmethod.EditorInfo
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val balanceInput = findViewById<EditText>(R.id.editBalanceGoal)
        var balance : Int

        balanceInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
            {
                val balanceText = balanceInput.toString()
                val value = balanceText.toIntOrNull() ?: 0
                balance = value
                true
            }
            else
            {
                false
            }
        }



//        val balanceText = balanceInput.toString()
//
//        if (balanceText.isNotEmpty())
//        {
//            balance = balanceText.toIntOrNull() ?: 0
//        }

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}