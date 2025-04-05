@file:OptIn(ExperimentalMaterial3Api::class)

package com.gclem19.smartpantry.views

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gclem19.smartpantry.R
import com.gclem19.smartpantry.data.PantryItem
import com.gclem19.smartpantry.data.ShoppingList
import com.gclem19.smartpantry.ui.theme.SmartPantryTheme
import com.gclem19.smartpantry.viewmodel.SmartPantryViewModel


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Home Pantry",
                            color = Color(0xFFD2B48C),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            )
        }
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
                // Pantry List Image Button
                Image(
                    painter = painterResource(id = R.drawable.pantry_list),
                    contentDescription = "Pantry List",
                    modifier = Modifier
                        .size(150.dp)
                        .border(3.dp, Color(0xFF006400))
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            Toast
                                .makeText(context, "Pantry List!", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate("pantry")
                        }
                )

                Spacer(modifier = Modifier.height(16.dp)) // Adds space between buttons

                // Shopping List Image Button
                Image(
                    painter = painterResource(id = R.drawable.shopping_list), // Replace with your image resource
                    contentDescription = "Shopping List",
                    modifier = Modifier
                        .size(150.dp)
                        .border(3.dp, Color(0xFF006400))
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            Toast
                                .makeText(context, "Shopping List!", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate("shopping list")
                        }
                )

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryList(navController: NavController, modifier: Modifier = Modifier, smartPantryViewModel: SmartPantryViewModel = viewModel()) {
    val pantryList by smartPantryViewModel.pantryList.collectAsState(initial = emptyList())

    // Group items by category
    val groupedItems = pantryList.groupBy { it.category }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Pantry List",
                            color = Color(0xFFD2B48C),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                groupedItems.forEach { (category, items) ->
                    item {
                        Text(
                            text = category,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF5C4033), // Dark brown
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(items) { item ->
                        Text(
                            text = "${item.name} - Qty: ${item.quantity} - Date: ${item.date}",
                            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("Add to pantry list") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Add",
                    color = Color(0xFFD2B48C)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPantryList(navController: NavController, modifier: Modifier = Modifier, smartPantryViewModel: SmartPantryViewModel = viewModel()) {
    val context = LocalContext.current
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text( text ="Add to Pantry List", color = Color(0xFFD2B48C)) }) }
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
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF5F5DC)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category TextField
            TextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF5F5DC)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity TextField
            TextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF5F5DC)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Item Name TextField
            TextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Expiry Date") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF5F5DC)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    val item =
                        PantryItem(
                            name = itemName,
                            category = category,
                            quantity = quantity,
                            date = date
                        )
                    smartPantryViewModel.addItemPantryList(item)
                    val marimbaSong = MediaPlayer.create(context, R.raw.marimba)
                    marimbaSong.start()
                    Toast.makeText(navController.context,
                        "Added: $itemName ($quantity) ($category) ($date)", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() // Navigate back
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Save to Pantry List",
                    color = Color(0xFFD2B48C) // Light brown color for text
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingList(navController: NavController, modifier: Modifier = Modifier, smartPantryViewModel: SmartPantryViewModel = viewModel()) {
    val shoppingList by smartPantryViewModel.shoppingList.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Shopping List",
                            color = Color(0xFFD2B48C),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Display list items
            LazyColumn(
                modifier = Modifier.weight(1f) // Takes available space, pushing the button down
            ) {
                items(shoppingList) { item ->
                    Text(
                        text = "${item.name} - ${item.quantity} ${item.category}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add button at the bottom
            Button(
                onClick = { navController.navigate("Add to shopping list") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Add",
                    color = Color(0xFFD2B48C)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShoppingList(navController: NavController, modifier: Modifier = Modifier, smartPantryViewModel: SmartPantryViewModel = viewModel()) {
    val context = LocalContext.current
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }


    Scaffold(
        topBar = { TopAppBar(title = {
            Text(text = "Add to Shopping list", color = Color(0xFFD2B48C)) }) }
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
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF5F5DC)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category TextField
            TextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF5F5DC)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity TextField
            TextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF5F5DC)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    val item = ShoppingList(name = itemName, category = category, quantity = quantity)
                    smartPantryViewModel.addItemShoppingList(item)
                    val song = MediaPlayer.create(context, R.raw.merengue) // Rename to `song` for consistency
                    song.start()
                    Toast.makeText(
                        navController.context,
                        "Added: $itemName ($quantity) ($category)",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack() // Navigate back
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Save to Shopping List",
                    color = Color(0xFFD2B48C) // Light brown color for text
                )
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


