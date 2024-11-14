package com.example.emailapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

class RegisterActivity : ComponentActivity() {
    private lateinit var databaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = UserDatabaseHelper(this)
        setContent {
            RegisterScreen(this, databaseHelper)
        }
    }
}

@Composable
fun RegisterScreen(context: Context, databaseHelper: UserDatabaseHelper) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
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
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                text = "Register",
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username", color = Color.Black) },
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color(0xFF4db6ac),
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.Black) },
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color(0xFF4db6ac),
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.Black) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color(0xFF4db6ac),
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                        val user = User(
                            id = null,
                            firstName = username,
                            lastName = null,
                            email = email,
                            password = password
                        )
                        databaseHelper.insertUser(user)
                        error = "User registered successfully"
                        context.startActivity(Intent(context, LoginActivity::class.java))
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
                Text(text = "Register", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                TextButton(onClick = {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                }) {
                    Text(color = Color(0xFF00796b), text = "Log in")
                }
            }
        }
    }
}

private fun startLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    ContextCompat.startActivity(context, intent, null)
}
