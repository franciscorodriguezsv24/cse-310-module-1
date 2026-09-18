package com.alejandro.eventcheckin.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** An event an organizer checks people in to. One row of the events table. */
@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val location: String,
    val date: String
)
