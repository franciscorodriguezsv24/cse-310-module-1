package com.alejandro.eventcheckin.data

/** An event an organizer checks people in to. */
data class Event(
    val id: Long = 0,
    val name: String,
    val location: String,
    val date: String
)
