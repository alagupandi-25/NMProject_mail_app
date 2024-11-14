package com.example.emailapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("logged_in_user_id", -1)
        Log.d("UserActivityLog", "User ID: $userId is accessing MailActivity")

        if (userId == -1) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContent {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
            ) {
                Email(this)
            }
        }
    }

    fun logout() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("logged_in_user_id", -1)
        editor.apply()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

@Composable
fun Email(context: Context) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome to Email App!",
            modifier = Modifier.padding(top = 40.dp),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(color = Color(0xFF4db6ac), shape = RoundedCornerShape(10.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.email),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                context.startActivity(
                    Intent(context, SendMailActivity::class.java)
                )
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4db6ac)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Send Email",
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                context.startActivity(
                    Intent(context, ViewMailActivity::class.java)
                )
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4db6ac)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "View Emails",
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                (context as MainActivity).logout()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Logout",
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontSize = 15.sp
            )
        }
    }
}
