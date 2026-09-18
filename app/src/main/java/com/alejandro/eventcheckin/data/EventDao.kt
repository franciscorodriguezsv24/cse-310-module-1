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

    @Query("SELECT * FROM events ORDER BY date, name")
    fun observeEvents(): Flow<List<Event>>

    @Insert
    suspend fun insert(event: Event): Long

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)
}
