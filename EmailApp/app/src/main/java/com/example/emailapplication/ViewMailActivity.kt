package com.example.emailapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.text.style.TextOverflow

class ViewMailActivity : ComponentActivity() {
    private lateinit var emailDatabaseHelper: EmailDatabaseHelper

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("logged_in_user_id", -1)

        Log.d("UserActivityLog", "User ID: $userId is accessing ViewMailActivity")

        if (userId == -1) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        emailDatabaseHelper = EmailDatabaseHelper(this)

        setContent {
            var showFavorites by remember { mutableStateOf(false) }
            var searchQuery by remember { mutableStateOf("") }
            var emails by remember { mutableStateOf(emailDatabaseHelper.getAllEmails()) }

            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = Color(0xFF4db6ac),
                        modifier = Modifier.height(80.dp),
                        title = {
                            Text(
                                text = "View Mails",
                                fontSize = 32.sp,
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    )
                },
                floatingActionButton = {
                    FilterButton(showFavorites) { showFavorites = it }
                },
                floatingActionButtonPosition = FabPosition.End,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search by Subject") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
                    )

                    val filteredEmails = if (showFavorites) {
                        emailDatabaseHelper.getFavoriteEmails()
                    } else {
                        emailDatabaseHelper.getAllEmails()
                    }.filter {
                        it.subject?.contains(searchQuery, ignoreCase = true) == true
                    }

                    emails = filteredEmails

                    EmailListSample(emails) { emailId ->
                        emailDatabaseHelper.deleteEmail(emailId)
                        emails = emailDatabaseHelper.getAllEmails()
                    }
                }
            }
        }
    }
}

@Composable
fun FilterButton(showFavorites: Boolean, onFilterChanged: (Boolean) -> Unit) {
    FloatingActionButton(
        onClick = {
            onFilterChanged(!showFavorites)
        },
        backgroundColor = Color(0xFF4db6ac),
        modifier = Modifier
            .padding(16.dp)
            .width(160.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Text(
            text = if (showFavorites) "Show All" else "Show Favorites",
            color = Color.White,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun EmailListSample(emails: List<Email>, onDelete: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(emails) { email ->
            EmailCard(email, onDelete)
        }
    }
}

@Composable
fun EmailCard(email: Email, onDelete: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            if (email.favorite) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(bottom = 8.dp),
                    tint = Color(0xFFFFD700)
                )
            }

            Text(
                text = "Receiver: ${email.recevierMail}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Subject: ${email.subject}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Body: ${email.body}",
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onDelete(email.id ?: 0) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
