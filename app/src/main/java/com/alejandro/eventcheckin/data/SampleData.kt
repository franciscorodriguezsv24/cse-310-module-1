package com.alejandro.eventcheckin.data

/** Hardcoded data used while the UI is being built and for @Preview composables. */
object SampleData {

    val events = listOf(
        Event(id = 1, name = "Ward Activity", location = "Cultural Hall", date = "Sep 20, 2026"),
        Event(id = 2, name = "CSE 310 Study Group", location = "Library 204", date = "Sep 22, 2026"),
        Event(id = 3, name = "Service Project", location = "City Park", date = "Sep 27, 2026")
    )

    val attendees = listOf(
        Attendee(id = 1, eventId = 1, name = "Maria Lopez", phone = "555-0100", checkedIn = true),
        Attendee(id = 2, eventId = 1, name = "John Smith", phone = "555-0101"),
        Attendee(id = 3, eventId = 1, name = "Sara Jones", phone = "555-0102", checkedIn = true),
        Attendee(id = 4, eventId = 2, name = "Luis Ramirez", phone = "555-0103"),
        Attendee(id = 5, eventId = 2, name = "Emily Chen", phone = "555-0104", checkedIn = true),
        Attendee(id = 6, eventId = 3, name = "David Park", phone = "555-0105")
    )

    /** The sample attendees that belong to one sample event. */
    fun attendeesFor(eventId: Long): List<Attendee> = attendees.filter { it.eventId == eventId }
}
