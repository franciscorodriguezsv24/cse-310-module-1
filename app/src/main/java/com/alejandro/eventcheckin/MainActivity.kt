package com.alejandro.eventcheckin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alejandro.eventcheckin.ui.theme.EventCheckInTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventCheckInTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Welcome(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Welcome(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(24.dp)) {
        Text(
            text = "Event Check-In",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "CSE 310 - Module 1 - Kotlin",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    EventCheckInTheme {
        Welcome()
    }
}
