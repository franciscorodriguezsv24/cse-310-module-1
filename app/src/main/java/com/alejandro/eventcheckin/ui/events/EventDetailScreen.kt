package com.alejandro.eventcheckin.ui.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alejandro.eventcheckin.data.Attendee
import com.alejandro.eventcheckin.data.Event
import com.alejandro.eventcheckin.data.SampleData
import com.alejandro.eventcheckin.ui.theme.EventCheckInTheme

/** Second screen: the attendees of one event, with the running check-in count. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    event: Event,
    attendees: List<Attendee>,
    onToggleCheckIn: (Attendee) -> Unit,
    onAddAttendee: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checkedIn = attendees.count { it.checkedIn }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(event.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAttendee) {
                Icon(Icons.Filled.Add, contentDescription = "Add attendee")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "$checkedIn of ${attendees.size} checked in",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = attendees, key = { it.id }) { attendee ->
                    AttendeeRow(attendee = attendee, onToggleCheckIn = { onToggleCheckIn(attendee) })
                }
            }
        }
    }
}

@Composable
private fun AttendeeRow(
    attendee: Attendee,
    onToggleCheckIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = attendee.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = attendee.phone,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Checkbox(checked = attendee.checkedIn, onCheckedChange = { onToggleCheckIn() })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventDetailPreview() {
    EventCheckInTheme {
        EventDetailScreen(
            event = SampleData.events.first(),
            attendees = SampleData.attendeesFor(1),
            onToggleCheckIn = {},
            onAddAttendee = {},
            onBack = {}
        )
    }
}
