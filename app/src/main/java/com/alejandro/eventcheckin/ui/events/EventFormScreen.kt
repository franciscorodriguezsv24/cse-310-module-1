package com.alejandro.eventcheckin.ui.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.alejandro.eventcheckin.data.Event
import com.alejandro.eventcheckin.ui.theme.EventCheckInTheme

/** One form used both to create an event and to edit an existing one. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFormScreen(
    title: String,
    event: Event?,
    onSave: (name: String, location: String, date: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable(event?.id) { mutableStateOf(event?.name.orEmpty()) }
    var location by rememberSaveable(event?.id) { mutableStateOf(event?.location.orEmpty()) }
    var date by rememberSaveable(event?.id) { mutableStateOf(event?.date.orEmpty()) }
    var showErrors by rememberSaveable(event?.id) { mutableStateOf(false) }

    val nameError = Validation.nameError(name)
    val locationError = Validation.locationError(location)
    val dateError = Validation.dateError(date)
    val isValid = nameError == null && locationError == null && dateError == null

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
            FormField(
                value = name,
                onValueChange = { name = it },
                label = "Event name",
                error = nameError.takeIf { showErrors }
            )
            FormField(
                value = location,
                onValueChange = { location = it },
                label = "Location",
                error = locationError.takeIf { showErrors }
            )
            FormField(
                value = date,
                onValueChange = { date = it },
                label = "Date",
                error = dateError.takeIf { showErrors },
                imeAction = ImeAction.Done
            )

            Button(
                onClick = {
                    showErrors = true
                    if (isValid) onSave(name, location, date)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}

/** A text field that shows its validation message under the input. */
@Composable
fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = error != null,
        supportingText = { if (error != null) Text(error) },
        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        modifier = modifier.fillMaxWidth()
    )
}

/** Preview of the empty form. */
@Preview(showBackground = true)
@Composable
private fun EventFormPreview() {
    EventCheckInTheme {
        EventFormScreen(title = "New event", event = null, onSave = { _, _, _ -> }, onBack = {})
    }
}
