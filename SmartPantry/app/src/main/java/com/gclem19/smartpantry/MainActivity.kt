package com.gclem19.smartpantry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.gclem19.smartpantry.viewmodel.SmartPantryViewModel
import com.gclem19.smartpantry.views.AppNavigation

class MainActivity : ComponentActivity() {

    private val SmartPantryViewModel: SmartPantryViewModel by viewModels()

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
    AppNavigation()
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