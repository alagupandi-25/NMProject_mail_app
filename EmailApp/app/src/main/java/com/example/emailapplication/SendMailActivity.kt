package com.example.emailapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SendMailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("logged_in_user_id", -1)
        Log.d("UserActivityLog", "User ID: $userId is accessing SendMailActivity")
        if (userId == -1) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        setContent {
            SendEmailUI(this)
        }
    }
}

@Composable
fun SendEmailUI(ctx: Context) {
    var receiverEmail by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Compose Email",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        OutlinedTextField(
            value = receiverEmail,
            onValueChange = { receiverEmail = it },
            label = { Text("Receiver Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            label = { Text("Body") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Mark as Favorite", modifier = Modifier.padding(end = 8.dp))
            Switch(checked = isFavorite, onCheckedChange = { isFavorite = it })
        }

        Button(
            onClick = {
                if (receiverEmail.isNotEmpty() && subject.isNotEmpty() && body.isNotEmpty()) {
                    val email = Email(
                        id = null,
                        recevierMail = receiverEmail,
                        subject = subject,
                        body = body,
                        favorite = isFavorite
                    )

                    val databaseHelper = EmailDatabaseHelper(ctx)
                    databaseHelper.insertEmail(email)
                    error = "Mail Saved"

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(receiverEmail))
                        putExtra(Intent.EXTRA_SUBJECT, subject)
                        putExtra(Intent.EXTRA_TEXT, body)
                        type = "message/rfc822"
                    }
                    ctx.startActivity(Intent.createChooser(intent, "Choose an Email client: "))
                } else {
                    error = "Please fill all fields"
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFd3e5ef)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Send Email", color = Color.Black, fontSize = 15.sp)
        }

        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = error, color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}
