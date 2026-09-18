package com.alejandro.eventcheckin.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alejandro.eventcheckin.EventCheckInApplication
import com.alejandro.eventcheckin.data.Attendee
import com.alejandro.eventcheckin.data.Event
import com.alejandro.eventcheckin.data.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Holds the state for every screen. The lists come from Room as a Flow and are
 * turned into StateFlow so the UI always has a value to draw, and so the state
 * survives configuration changes such as screen rotation.
 */
class EventViewModel(private val repository: EventRepository) : ViewModel() {

    /** Every event, kept hot for five seconds after the last screen stops watching. */
    val events: StateFlow<List<Event>> = repository.events.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = emptyList()
    )

    /** Every attendee of every event. Each screen filters the ones it needs. */
    val attendees: StateFlow<List<Attendee>> = repository.attendees.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = emptyList()
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    /** The list the first screen draws: every event that matches the search box. */
    val filteredEvents: StateFlow<List<Event>> =
        combine(repository.events, _searchQuery) { events, query ->
            if (query.isBlank()) {
                events
            } else {
                events.filter {
                    it.name.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true) ||
                        it.date.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    /** Called on every keystroke in the search box. */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /** Creates an event. The forms validate first, so the text arrives clean. */
    fun addEvent(name: String, location: String, date: String) = viewModelScope.launch {
        repository.addEvent(name, location, date)
    }

    /** Saves the edits made to an existing event. */
    fun updateEvent(event: Event, name: String, location: String, date: String) =
        viewModelScope.launch {
            repository.updateEvent(
                event.copy(name = name.trim(), location = location.trim(), date = date.trim())
            )
        }

    /** Deletes an event. Its attendees go with it through the foreign key. */
    fun deleteEvent(event: Event) = viewModelScope.launch {
        repository.deleteEvent(event)
    }

    /** Saves the edits made to an attendee, keeping the check-in state as it is. */
    fun updateAttendee(attendee: Attendee, name: String, phone: String) = viewModelScope.launch {
        repository.updateAttendee(attendee.copy(name = name.trim(), phone = phone.trim()))
    }

    /** Removes one person from an event. */
    fun deleteAttendee(attendee: Attendee) = viewModelScope.launch {
        repository.deleteAttendee(attendee)
    }

    /** Checks a person in, or undoes it if they were already checked in. */
    fun toggleCheckIn(attendee: Attendee) = viewModelScope.launch {
        repository.setCheckedIn(attendee.id, !attendee.checkedIn)
    }

    /** Registers a new person for an event. */
    fun addAttendee(eventId: Long, name: String, phone: String) = viewModelScope.launch {
        repository.addAttendee(eventId, name, phone)
    }

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5_000L

        /** Gives the ViewModel the repository that lives in the Application. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    as EventCheckInApplication
                EventViewModel(application.repository)
            }
        }
    }
}
