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

    /** Trims the text from the form and stores a new event. */
    suspend fun addEvent(name: String, location: String, date: String): Long =
        eventDao.insert(Event(name = name.trim(), location = location.trim(), date = date.trim()))

    /** Saves an edited event. */
    suspend fun updateEvent(event: Event) = eventDao.update(event)

    /** Deletes an event together with its attendees. */
    suspend fun deleteEvent(event: Event) = eventDao.delete(event)

    /** Registers a person for an event, new rows always start as not checked in. */
    suspend fun addAttendee(eventId: Long, name: String, phone: String): Long =
        attendeeDao.insert(
            Attendee(eventId = eventId, name = name.trim(), phone = phone.trim())
        )

    /** Saves an edited attendee. */
    suspend fun updateAttendee(attendee: Attendee) = attendeeDao.update(attendee)

    /** Removes one person from an event. */
    suspend fun deleteAttendee(attendee: Attendee) = attendeeDao.delete(attendee)

    /** Marks one attendee as checked in or not checked in. */
    suspend fun setCheckedIn(attendeeId: Long, checkedIn: Boolean) =
        attendeeDao.setCheckedIn(attendeeId, checkedIn)
}
