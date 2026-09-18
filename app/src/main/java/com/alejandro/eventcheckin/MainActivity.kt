package com.alejandro.eventcheckin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alejandro.eventcheckin.ui.EventCheckInApp
import com.alejandro.eventcheckin.ui.theme.EventCheckInTheme

/** The single activity of the app. Everything inside it is Compose. */
class MainActivity : ComponentActivity() {
    /** Sets the Compose content once, then the navigation graph takes over. */
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
