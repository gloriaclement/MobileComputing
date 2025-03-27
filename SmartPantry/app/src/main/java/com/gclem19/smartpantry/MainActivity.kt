package com.gclem19.smartpantry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.gclem19.smartpantry.ui.theme.SmartPantryTheme
import com.gclem19.smartpantry.views.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartPantryApp() // This should be your main composable function
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