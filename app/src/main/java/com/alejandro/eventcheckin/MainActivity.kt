package com.alejandro.eventcheckin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alejandro.eventcheckin.ui.EventCheckInApp
import com.alejandro.eventcheckin.ui.theme.EventCheckInTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventCheckInTheme {
                EventCheckInApp()
            }
        }
    }
}
