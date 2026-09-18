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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alejandro.eventcheckin.data.Event
import com.alejandro.eventcheckin.data.SampleData
import com.alejandro.eventcheckin.ui.theme.EventCheckInTheme

/**
 * First screen of the app: every event with its live check-in count.
 * It still reads from SampleData; the ViewModel and the database come later.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    events: List<Event>,
    checkedInCount: (Long) -> Int,
    totalCount: (Long) -> Int,
    onEventClick: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Events") }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = events, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    checkedIn = checkedInCount(event.id),
                    total = totalCount(event.id),
                    onClick = { onEventClick(event) }
                )
            }
        }
    }
}

@Composable
private fun EventCard(
    event: Event,
    checkedIn: Int,
    total: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(end = 12.dp)) {
                Text(text = event.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${event.date} - ${event.location}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "$checkedIn/$total",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventListPreview() {
    EventCheckInTheme {
        EventListScreen(
            events = SampleData.events,
            checkedInCount = { id -> SampleData.attendeesFor(id).count { it.checkedIn } },
            totalCount = { id -> SampleData.attendeesFor(id).size },
            onEventClick = {}
        )
    }
}
