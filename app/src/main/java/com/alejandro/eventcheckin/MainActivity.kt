package com.alejandro.eventcheckin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alejandro.eventcheckin.data.SampleData
import com.alejandro.eventcheckin.ui.events.EventListScreen
import com.alejandro.eventcheckin.ui.theme.EventCheckInTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventCheckInTheme {
                EventListScreen(
                    events = SampleData.events,
                    checkedInCount = { id -> SampleData.attendeesFor(id).count { it.checkedIn } },
                    totalCount = { id -> SampleData.attendeesFor(id).size },
                    onEventClick = {}
                )
            }
        }
    }
}
