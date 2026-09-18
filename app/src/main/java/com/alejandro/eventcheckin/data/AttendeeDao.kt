package com.alejandro.eventcheckin.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** Reads and writes for the attendees table. */
@Dao
interface AttendeeDao {

    /** Emits the whole list again every time any attendee row changes. */
    @Query("SELECT * FROM attendees ORDER BY name")
    fun observeAttendees(): Flow<List<Attendee>>

    /** Inserts an attendee and returns the generated id. */
    @Insert
    suspend fun insert(attendee: Attendee): Long

    /** Saves an edited attendee. */
    @Update
    suspend fun update(attendee: Attendee)

    /** Removes one attendee. */
    @Delete
    suspend fun delete(attendee: Attendee)

    /** Flips only the check-in column, so a check-in never rewrites the name. */
    @Query("UPDATE attendees SET checkedIn = :checkedIn WHERE id = :attendeeId")
    suspend fun setCheckedIn(attendeeId: Long, checkedIn: Boolean)
}
