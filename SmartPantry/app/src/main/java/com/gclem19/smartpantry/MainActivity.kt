package com.gclem19.smartpantry


import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gclem19.smartpantry.viewmodel.SmartPantryViewModel
import com.gclem19.smartpantry.views.AppNavigation

class MainActivity : ComponentActivity() {
    private val smartPantryViewModel: SmartPantryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the notification channel when the app starts
        createNotificationChannel()

        // Trigger the expiry check and notifications
        smartPantryViewModel.checkAndNotifyExpiringItems(applicationContext)

        setContent {
            SmartPantryApp() // This should be your main composable function
            //code for tasks here
            //for example, insert a user record into User_table
            //viewModel.insert(User(username ="Sarah", score = 80, duration = 70))
        }
    }

    // Create notification channel (required for Android 8.0 and above)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "expiring_food_channel"
            val channelName = "Expiring Food Notifications"
            val channelDescription = "Notifications for food items that are expiring soon."
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(
                channelId,
                channelName,
                importance
            ).apply {
                description = channelDescription
            }

            // Register the channel with the system
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}


@Composable
fun SmartPantryApp() {
    // Define the dark color scheme
    val darkColorScheme = darkColorScheme(
        primary = Color(0xFF5C4033), // Dark Brown
        secondary = Color(0xFF808000), // Olive Green
        tertiary = Color(0xFFD2B48C), // Light Brown
        background = Color(0xFFC8D472), // Olive Green as background
        surface = Color(0xFF808000) // Olive Green for surfaces too
    )

    // Apply the color scheme to MaterialTheme
    MaterialTheme(
        colorScheme = darkColorScheme
    ) {
        // Call AppNavigation inside the MaterialTheme to apply the theme to all screens
        AppNavigation()
    }
}

//@Composable
//fun LoginScreen(navController: NavHostController) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Login", style = MaterialTheme.typography.headlineMedium)
//        Button(onClick = { navController.navigate("main_menu") }) {
//            Text("Login")
//        }
//        TextButton(onClick = { navController.navigate("register") }) {
//            Text("Don't have an account? Register")
//        }
//    }
//}
//
//@Composable
//fun RegisterScreen(navController: NavHostController) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Register", style = MaterialTheme.typography.headlineMedium)
//        Button(onClick = { navController.navigate("main_menu") }) {
//            Text("Register")
//        }
//        TextButton(onClick = { navController.navigate("login") }) {
//            Text("Already have an account? Login")
//        }
//    }
//}
//
//@Composable
//fun MainMenuScreen(navController: NavHostController) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Main Menu", style = MaterialTheme.typography.headlineMedium)
//        Button(onClick = { navController.navigate("login") }) {
//            Text("Logout")
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun SmartPantryAppPreview() {
//    SmartPantryTheme {
//        SmartPantryApp()
//    }
//}