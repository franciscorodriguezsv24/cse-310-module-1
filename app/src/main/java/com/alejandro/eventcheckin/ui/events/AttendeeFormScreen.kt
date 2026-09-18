package com.alejandro.eventcheckin.ui.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alejandro.eventcheckin.data.Attendee
import com.alejandro.eventcheckin.ui.theme.EventCheckInTheme

/** One form used both to register a new attendee and to edit an existing one. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendeeFormScreen(
    title: String,
    eventName: String,
    attendee: Attendee?,
    onSave: (name: String, phone: String) -> Unit,
    onDelete: (() -> Unit)? = null,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // rememberSaveable keeps what was typed when the screen rotates.
    var name by rememberSaveable(attendee?.id) { mutableStateOf(attendee?.name.orEmpty()) }
    var phone by rememberSaveable(attendee?.id) { mutableStateOf(attendee?.phone.orEmpty()) }
    var showErrors by rememberSaveable(attendee?.id) { mutableStateOf(false) }

    val nameError = Validation.nameError(name)
    val phoneError = Validation.phoneError(phone)
    val isValid = nameError == null && phoneError == null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = eventName, style = MaterialTheme.typography.titleMedium)

            FormField(
                value = name,
                onValueChange = { name = it },
                label = "Name",
                error = nameError.takeIf { showErrors }
            )

            FormField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone",
                error = phoneError.takeIf { showErrors },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                imeAction = ImeAction.Done
            )

            Button(
                onClick = {
                    showErrors = true
                    if (isValid) onSave(name, phone)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }

            if (onDelete != null) {
                OutlinedButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                    Text("Delete attendee", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AttendeeFormPreview() {
    EventCheckInTheme {
        AttendeeFormScreen(
            title = "Add attendee",
            eventName = "Ward Activity",
            attendee = null,
            onSave = { _, _ -> },
            onBack = {}
        )
    }
}
