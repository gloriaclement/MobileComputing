@file:OptIn(ExperimentalMaterial3Api::class)

package com.gclem19.smartpantry.views



import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.magnifier
import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.gclem19.smartpantry.R
import com.gclem19.smartpantry.ui.theme.SmartPantryTheme
//define app navigation
@Composable
fun AppNavigation() {
    //retrieve navController
    val navController = rememberNavController()
    //set navHost and the routes
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("pantry") { PantryList(navController) }
        composable("shopping list") { ShoppingList(navController) }
        composable("add to shopping list") { AddShoppingList(navController) }
        composable("add to pantry list") { AddPantryList(navController) }
    }
}



@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Smart Pantry Home") }) }
    ) { padding -> // Padding from Scaffold
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding), // Apply scaffold padding
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pantry List Button
                Button(onClick = {
                    Toast.makeText(context, "Pantry List!", Toast.LENGTH_SHORT).show()
                    navController.navigate("pantry")
                }) {
                    Text("Pantry List")
                }

                Spacer(modifier = Modifier.height(16.dp)) // Adds space between buttons

                // Shopping List Button
                Button(onClick = {
                    Toast.makeText(context, "Shopping List!", Toast.LENGTH_SHORT).show()
                    navController.navigate("shopping list")
                }) {
                    Text("Shopping List")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryList(navController: NavController, modifier: Modifier = Modifier){
    Scaffold(topBar = { TopAppBar(title = {Text("Pantry List")}) }) {
            padding ->
        Column(modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Button(onClick = {navController.navigate("add to pantry list")}) {
                Text("Add")
            }
        }
    }
}

@Composable
fun ShoppingList(navController: NavController, modifier: Modifier = Modifier){
    Scaffold(topBar = { TopAppBar(title = {Text("Shopping List")}) }) {
            padding ->
        Column(modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Button(onClick = {navController.navigate("add to shopping list")}) {
                Text("Add")
            }
        }
    }
}


@Composable
fun AddShoppingList(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(topBar = { TopAppBar(title = {Text("Add to shopping list")}) }) {
            padding ->
        Column(modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Button(onClick = {navController.navigate("home")}) {
                Text("Back")
            }
        }
    }
}

@Composable
fun AddPantryList(navController: NavController, modifier: Modifier = Modifier) {
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add to Pantry") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Item Name TextField
            TextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity TextField
            TextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    Toast.makeText(navController.context, "Added: $itemName ($quantity)", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() // Navigate back
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save to Pantry")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    SmartPantryTheme {
        AppNavigation()
    }
}
