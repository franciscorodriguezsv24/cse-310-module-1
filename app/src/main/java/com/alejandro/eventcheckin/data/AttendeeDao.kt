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

    @Query("SELECT * FROM attendees ORDER BY name")
    fun observeAttendees(): Flow<List<Attendee>>

    @Insert
    suspend fun insert(attendee: Attendee): Long

    @Update
    suspend fun update(attendee: Attendee)

    @Delete
    suspend fun delete(attendee: Attendee)

    @Query("UPDATE attendees SET checkedIn = :checkedIn WHERE id = :attendeeId")
    suspend fun setCheckedIn(attendeeId: Long, checkedIn: Boolean)
}
