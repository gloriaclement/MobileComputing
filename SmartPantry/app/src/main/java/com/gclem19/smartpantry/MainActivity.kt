package com.gclem19.smartpantry


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
        setContent {
            SmartPantryApp() // This should be your main composable function
            //code for tasks here
            //for example, insert a user record into User_table
            //viewModel.insert(User(username ="Sarah", score = 80, duration = 70))
        }
    }
}

//
@Composable
fun SmartPantryApp() {
    // Define the dark color scheme
    val darkColorScheme = darkColorScheme(
        primary = Color(0xFF5C4033), // Dark Brown
        secondary = Color(0xFF808000), // Olive Green
        tertiary = Color(0xFFD2B48C), // Light Brown
        background = Color(0xFF808000), // Olive Green as background
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