package com.alejandro.eventcheckin.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One registered person, always attached to an event.
 * Deleting the event deletes its attendees through the foreign key.
 */
@Entity(
    tableName = "attendees",
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("eventId")]
)
data class Attendee(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventId: Long,
    val name: String,
    val phone: String,
    val checkedIn: Boolean = false
)
