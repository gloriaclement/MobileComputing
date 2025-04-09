@file:OptIn(ExperimentalMaterial3Api::class)

package com.gclem19.smartpantry.views


import android.app.DatePickerDialog
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
import java.util.Calendar


//define app navigation
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "launcher") {
        composable("launcher") { LauncherScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("registration") { RegistrationScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("pantry") { PantryList(navController) }
        composable("shopping list") { ShoppingList(navController) }
        composable("add to shopping list") { AddShoppingList(navController) }
        composable("add to pantry list") { AddPantryList(navController) }
    }
}

@Composable
fun LauncherScreen(navController: NavController, smartPantryViewModel: SmartPantryViewModel = viewModel()) {

    val isLoggedIn by smartPantryViewModel.isLoggedIn.collectAsState(initial = false)
    val isUserRegistered by smartPantryViewModel.isUserRegistered.collectAsState(initial = false)

    LaunchedEffect(isUserRegistered, isLoggedIn) {
        if (!isUserRegistered) {
            navController.navigate("registration") {
                popUpTo("launcher") { inclusive = true }
            }
        } else if (isLoggedIn) {
            navController.navigate("home") {
                popUpTo("launcher") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("launcher") { inclusive = true }
            }
        }
    }

    // Optional UI if you want a splash screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF808000)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavController, smartPantryViewModel: SmartPantryViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current

    val appRegLogo = painterResource(id = R.drawable.app_logo)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF808000)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Reg Label
        Text(
            text = "Register for Smart Pantry",
            style = TextStyle(
                fontSize = 32.sp, // You can adjust the font size
                fontWeight = FontWeight.Bold, // Make it bold
                color = Color.DarkGray // Set the text color
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // RegLogo
        Image(
            painter = appRegLogo,
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp) // Adjust the size as needed
                .clip(RoundedCornerShape(16.dp)) // Apply rounded corners to the image
                .padding(bottom = 32.dp) // Space between the logo and the text fields
        )

        // Username TextField
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor =  Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor =  Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone TextField
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor =  Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date of Birth TextField
        TextField(
            value = dob,
            onValueChange = { dob = it },
            label = { Text("Date of Birth (YYYY-MM-DD)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor =  Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor =  Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password TextField
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor =  Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (username.isNotBlank() && email.isNotBlank() && phone.isNotBlank() &&
                    dob.isNotBlank() && password == confirmPassword) {

                    smartPantryViewModel.register(username, email, phone, dob, password) {
                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                        navController.navigate("home") {
                            popUpTo("registration") { inclusive = true }
                        }
                    }

                } else {
                    Toast.makeText(context, "Please fill in all fields and make sure passwords match", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Text("Register")
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier, smartPantryViewModel: SmartPantryViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current


    // Replace this with your app logo resource or drawable
    val appLogo = painterResource(id = R.drawable.app_logo) // Make sure you have a logo in your resources

    Column(
        modifier = Modifier
            .fillMaxSize() // Make sure this fills the entire screen
            .background(Color(0xFF808000)), // Set the background color to #808000 (Olive Green)
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Welcome Label
        Text(
            text = "Welcome to Smart Pantry",
            style = TextStyle(
                fontSize = 32.sp, // You can adjust the font size
                fontWeight = FontWeight.Bold, // Make it bold
                color = Color.DarkGray // Set the text color
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // App Logo
        Image(
            painter = appLogo,
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp) // Adjust the size as needed
                .clip(RoundedCornerShape(16.dp)) // Apply rounded corners to the image
                .padding(bottom = 32.dp) // Space between the logo and the text fields
        )

        // Username TextField
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor =  Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor =  Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

// Login Button
        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    println("Attempting login with Username: $username and Password: $password") // Add this for debugging
                    smartPantryViewModel.login(username, password,
                        onSuccess = {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onError = {
                            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    Toast.makeText(context, "Please enter both username and password", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Text("Login")
        }



        Spacer(modifier = Modifier.height(16.dp))

        // Navigate to registration screen
        TextButton(onClick = { navController.navigate("registration") }) {
            Text("Don't have an account? Register here.")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    smartPantryViewModel: SmartPantryViewModel = viewModel()
) {
    val context = LocalContext.current
    val expiringItems = smartPantryViewModel.getExpiringItems()
    val recipeMap by smartPantryViewModel.recipesForExpiringItems.collectAsState()

    // Trigger notification for expiring items when HomeScreen is launched
    LaunchedEffect(Unit) {
        smartPantryViewModel.checkAndNotifyExpiringItems(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Home Pantry",
                        color = Color(0xFFD2B48C),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    // Add a logout button in the top app bar
                    IconButton(onClick = {
                        // Trigger logout and navigate to login screen
                        smartPantryViewModel.logout {
                            navController.navigate("login") {
                                // Ensure the back stack is cleared so the user can't go back to the home screen
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()) // Enable scrolling
                .padding(16.dp), // Optional padding for content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Expiry Notification Section
            if (expiringItems.isNotEmpty()) {
                Text(
                    text = "Expiring Soon:",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
                expiringItems.forEach { item ->
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "${item.name} - ${item.date}",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        val recipes = recipeMap[item.name]
                        if (!recipes.isNullOrEmpty()) {
                            Text(
                                text = "Suggested Recipes:",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF006400),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            recipes.take(5).forEach { recipe ->
                                Text(
                                    text = "- $recipe",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "Fetching recipes...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No items expiring soon.",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Pantry List Image Button
            Image(
                painter = painterResource(id = R.drawable.pantry_list),
                contentDescription = "Pantry List",
                modifier = Modifier
                    .size(150.dp)
                    .border(3.dp, Color(0xFF006400), RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        Toast
                            .makeText(context, "Pantry List!", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate("pantry")
                    }
            )

            // Shopping List Image Button
            Image(
                painter = painterResource(id = R.drawable.shopping_list),
                contentDescription = "Shopping List",
                modifier = Modifier
                    .size(150.dp)
                    .border(3.dp, Color(0xFF006400), RoundedCornerShape(16.dp))
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryList(navController: NavController, modifier: Modifier = Modifier, smartPantryViewModel: SmartPantryViewModel = viewModel()
) {
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
                .verticalScroll(rememberScrollState())
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.name} - Qty: ${item.quantity} - Date: ${item.date}",
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(onClick = {
                                smartPantryViewModel.removeFromPantryList(item)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete item",
                                    tint = Color.Red
                                )
                            }
                        }
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
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPantryList(navController: NavController, modifier: Modifier = Modifier, smartPantryViewModel: SmartPantryViewModel = viewModel()) {
    val context = LocalContext.current
    val autofillItem by smartPantryViewModel.autofillItem.collectAsState()

    var itemName by remember { mutableStateOf(autofillItem?.name ?: "") }
    var quantity by remember { mutableStateOf(autofillItem?.quantity ?: "") }
    var category by remember { mutableStateOf(autofillItem?.category ?: "") }
//    var itemName by remember { mutableStateOf("") }
//    var quantity by remember { mutableStateOf("") }
//    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    smartPantryViewModel.clearAutofill()

    // DatePickerDialog setup
    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDay = String.format("%02d", selectedDay)
            val formattedMonth = String.format("%02d", selectedMonth + 1) // Month is 0-based
            date = "$formattedDay/$formattedMonth/$selectedYear"
        },
        year, month, day
    )

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
                    containerColor = Color(0xFFF5F5DC),
                    focusedTextColor =  Color.Black,
                    unfocusedTextColor = Color.Black
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
                    containerColor = Color(0xFFF5F5DC),
                    focusedTextColor =  Color.Black,
                    unfocusedTextColor = Color.Black
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
                    containerColor = Color(0xFFF5F5DC),
                    focusedTextColor =  Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Expiry Date TextField
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() }
            ) {
                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    label = { Text("Expiry Date") },
                    singleLine = true,
                    readOnly = true,
                    enabled = false, // Prevents text field from getting focus
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledTextColor = Color.Black,
                        disabledLabelColor = Color.Gray,
                        disabledBorderColor = Color.Gray,
                        disabledTrailingIconColor = Color.Black,
                        disabledLeadingIconColor = Color.Black,
                        disabledPlaceholderColor = Color.DarkGray,
                        containerColor = Color(0xFFF5F5DC),
                        unfocusedTextColor = Color.Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    // Date validation
                    if (date.isNotEmpty()) {
                        val newItem = PantryItem(
                            name = itemName,
                            category = category,
                            quantity = quantity,
                            date = date
                        )
                        smartPantryViewModel.addItemPantryList(newItem)
                        val marimbaSong = MediaPlayer.create(context, R.raw.marimba)
                        marimbaSong.start()
                        Toast.makeText(
                            navController.context,
                            "Added: $itemName ($quantity) ($category) ($date)",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.popBackStack() // Navigate back
                    } else {
                        // Show error message if date is invalid
                        Log.e("DateValidation", "Invalid date format: $date")
                        Toast.makeText(
                            navController.context,
                            "Please enter a valid date (yyyy-MM-dd).",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
fun ShoppingList(navController: NavController, modifier: Modifier = Modifier, smartPantryViewModel: SmartPantryViewModel = viewModel()
) {
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
            // Display list items grouped by category
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                val groupedItems = shoppingList.groupBy { it.category }

                groupedItems.forEach { (category, itemsInCategory) ->
                    // Category header
                    item {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            color = Color.Black
                        )
                    }

                    // Items under the category
                    items(itemsInCategory) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.name} - ${item.quantity}",
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF7A4A09)
                            )

                            // Move to pantry
                            IconButton(onClick = {
                                smartPantryViewModel.setItemToAutofill(item)
                                smartPantryViewModel.removeFromShoppingList(item)
                                navController.navigate("add to pantry list")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Move to pantry",
                                    tint = Color.Black
                                )
                            }

                            // Delete
                            IconButton(
                                onClick = {
                                    smartPantryViewModel.removeFromShoppingList(item)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete item",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add button at the bottom
            Button(
                onClick = { navController.navigate("add to shopping list") },
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
                    containerColor = Color(0xFFF5F5DC),
                    focusedTextColor =  Color.Black,
                    unfocusedTextColor = Color.Black
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
                    containerColor = Color(0xFFF5F5DC),
                    focusedTextColor =  Color.Black,
                    unfocusedTextColor = Color.Black
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
                    containerColor = Color(0xFFF5F5DC),
                    focusedTextColor =  Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    val item = ShoppingList(name = itemName, category = category, quantity = quantity)
                    smartPantryViewModel.addItemShoppingList(item)
                    val song = MediaPlayer.create(context, R.raw.merengue)
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
