package com.alejandro.eventcheckin.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** Reads and writes for the events table. Queries return a Flow so the UI updates itself. */
@Dao
interface EventDao {

    /** Emits the whole list again every time any event row changes. */
    @Query("SELECT * FROM events ORDER BY date, name")
    fun observeEvents(): Flow<List<Event>>

    /** Inserts an event and returns the id SQLite generated for it. */
    @Insert
    suspend fun insert(event: Event): Long

    /** Saves an edited event, matched by its primary key. */
    @Update
    suspend fun update(event: Event)

    /** Deletes an event and, through the foreign key, its attendees. */
    @Delete
    suspend fun delete(event: Event)
}
