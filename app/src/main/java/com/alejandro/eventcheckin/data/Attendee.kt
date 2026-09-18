package com.alejandro.eventcheckin.data

/** One registered person, always attached to an event. */
data class Attendee(
    val id: Long = 0,
    val eventId: Long,
    val name: String,
    val phone: String,
    val checkedIn: Boolean = false
)
