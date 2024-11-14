package com.example.emailapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

class LoginActivity : ComponentActivity() {
    private lateinit var databaseHelper: UserDatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = UserDatabaseHelper(this)
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val userId = sharedPreferences.getInt("logged_in_user_id", -1)
        Log.d("UserActivityLog", "User ID: $userId is accessing loginActivity")
        if (userId != -1) {
            startMainPage(this)
            finish()
        } else {
            setContent {
                LoginScreen(this, databaseHelper, sharedPreferences)
            }
        }
    }
}

@Composable
fun LoginScreen(context: Context, databaseHelper: UserDatabaseHelper, sharedPreferences: SharedPreferences) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.email),
                contentDescription = null,
                modifier = Modifier.size(300.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                text = "Login",
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username", color = Color.Black) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.White,
                    placeholderColor = Color.Gray,
                    focusedIndicatorColor = Color(0xFF4db6ac),
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.Black) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.White,
                    placeholderColor = Color.Gray,
                    focusedIndicatorColor = Color(0xFF4db6ac),
                    unfocusedIndicatorColor = Color.LightGray
                )
            )


            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        val user = databaseHelper.getUserByUsername(username)
                        if (user != null && user.password == password) {

                            with(sharedPreferences.edit()) {
                                putInt("logged_in_user_id", user.id ?: -1)
                                apply()
                            }
                            context.startActivity(Intent(context, MainActivity::class.java))
                        } else {
                            error = "Invalid username or password"
                        }
                    } else {
                        error = "Please fill all fields"
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4db6ac)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(48.dp)
            ) {
                Text(text = "Login", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                TextButton(onClick = {
                    context.startActivity(Intent(context, RegisterActivity::class.java))
                }) {
                    Text(color = Color(0xFF00796b), text = "Sign up")
                }
            }
        }
    }
}

private fun startMainPage(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    ContextCompat.startActivity(context, intent, null)
}
