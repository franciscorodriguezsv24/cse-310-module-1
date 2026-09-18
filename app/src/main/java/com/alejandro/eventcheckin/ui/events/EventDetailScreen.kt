package com.alejandro.eventcheckin.ui.events

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    onAttendeeClick: (Attendee) -> Unit,
    onAddAttendee: () -> Unit,
    onEditEvent: () -> Unit,
    onDeleteEvent: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checkedIn = attendees.count { it.checkedIn }
    var confirmDelete by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(event.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditEvent) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit event")
                    }
                    IconButton(onClick = { confirmDelete = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete event")
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
            if (attendees.isEmpty()) {
                EmptyMessage(
                    title = "No attendees yet",
                    detail = "Tap the + button to register the first person for this event."
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = attendees, key = { it.id }) { attendee ->
                        AttendeeRow(
                            attendee = attendee,
                            onToggleCheckIn = { onToggleCheckIn(attendee) },
                            onClick = { onAttendeeClick(attendee) }
                        )
                    }
                }
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete event?") },
            text = { Text("\"${'$'}{event.name}\" and its ${'$'}{attendees.size} attendees will be removed.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmDelete = false
                        onDeleteEvent()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun AttendeeRow(
    attendee: Attendee,
    onToggleCheckIn: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
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
            onAttendeeClick = {},
            onAddAttendee = {},
            onEditEvent = {},
            onDeleteEvent = {},
            onBack = {}
        )
    }
}
