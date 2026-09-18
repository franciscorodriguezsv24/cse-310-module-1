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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Holds the state for every screen. The lists come from Room as a Flow and are
 * turned into StateFlow so the UI always has a value to draw, and so the state
 * survives configuration changes such as screen rotation.
 */
class EventViewModel(private val repository: EventRepository) : ViewModel() {

    val events: StateFlow<List<Event>> = repository.events.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = emptyList()
    )

    val attendees: StateFlow<List<Attendee>> = repository.attendees.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = emptyList()
    )

    fun toggleCheckIn(attendee: Attendee) = viewModelScope.launch {
        repository.setCheckedIn(attendee.id, !attendee.checkedIn)
    }

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
