package com.alejandro.eventcheckin.ui.events

import androidx.lifecycle.ViewModel
import com.alejandro.eventcheckin.data.Attendee
import com.alejandro.eventcheckin.data.Event
import com.alejandro.eventcheckin.data.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Holds the state for every screen. The ViewModel survives configuration
 * changes such as screen rotation, which is why the lists live here and not
 * inside a composable. The database replaces this in-memory copy later.
 */
class EventViewModel : ViewModel() {

    private val _events = MutableStateFlow(SampleData.events)
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _attendees = MutableStateFlow(SampleData.attendees)
    val attendees: StateFlow<List<Attendee>> = _attendees.asStateFlow()

    private var nextAttendeeId: Long = (SampleData.attendees.maxOfOrNull { it.id } ?: 0) + 1

    fun eventById(eventId: Long): Event? = _events.value.find { it.id == eventId }

    fun attendeesFor(eventId: Long): List<Attendee> =
        _attendees.value.filter { it.eventId == eventId }

    fun checkedInCount(eventId: Long): Int =
        _attendees.value.count { it.eventId == eventId && it.checkedIn }

    fun totalCount(eventId: Long): Int = _attendees.value.count { it.eventId == eventId }

    fun toggleCheckIn(attendeeId: Long) {
        _attendees.update { list ->
            list.map { if (it.id == attendeeId) it.copy(checkedIn = !it.checkedIn) else it }
        }
    }

    fun addAttendee(eventId: Long, name: String, phone: String) {
        _attendees.update { list ->
            list + Attendee(
                id = nextAttendeeId++,
                eventId = eventId,
                name = name.trim(),
                phone = phone.trim()
            )
        }
    }
}
