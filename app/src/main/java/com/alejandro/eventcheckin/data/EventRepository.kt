package com.alejandro.eventcheckin.data

import kotlinx.coroutines.flow.Flow

/**
 * Single place the ViewModel talks to. It hides Room behind plain functions,
 * so the UI never touches a DAO directly.
 */
class EventRepository(
    private val eventDao: EventDao,
    private val attendeeDao: AttendeeDao
) {
    val events: Flow<List<Event>> = eventDao.observeEvents()
    val attendees: Flow<List<Attendee>> = attendeeDao.observeAttendees()

    suspend fun addEvent(name: String, location: String, date: String): Long =
        eventDao.insert(Event(name = name.trim(), location = location.trim(), date = date.trim()))

    suspend fun updateEvent(event: Event) = eventDao.update(event)

    suspend fun deleteEvent(event: Event) = eventDao.delete(event)

    suspend fun addAttendee(eventId: Long, name: String, phone: String): Long =
        attendeeDao.insert(
            Attendee(eventId = eventId, name = name.trim(), phone = phone.trim())
        )

    suspend fun updateAttendee(attendee: Attendee) = attendeeDao.update(attendee)

    suspend fun deleteAttendee(attendee: Attendee) = attendeeDao.delete(attendee)

    suspend fun setCheckedIn(attendeeId: Long, checkedIn: Boolean) =
        attendeeDao.setCheckedIn(attendeeId, checkedIn)
}
