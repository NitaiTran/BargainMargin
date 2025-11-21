package com.progprof.bargainmargintemplate
import android.os.Build



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.progprof.bargainmargintemplate.ui.* //imports everything from the ui package
import com.progprof.bargainmargintemplate.ui.theme.AppTheme
// import androidx.room.Room  // <<< DELETE THIS LINE
// import com.progprof.bargainmargintemplate.ui.BudgetNotificationManager // Already imported by wildcard
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat


class MainActivity : ComponentActivity() {

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, you can send notifications now
            } else {
                // Permission denied, consider disabling notifications or show rationale
            }
        }

    // *** START DELETION ***
    /*
    companion object {
        lateinit var database: AppDatabase
    }
    */
    // *** END DELETION ***

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BudgetNotificationManager.createNotificationChannel(this)
        requestNotificationPermissionIfNeeded()

        // *** START DELETION ***
        /*
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "budget_tracker_db"
        ).build()
        */
        // *** END DELETION ***


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
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

                else -> {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }


}
