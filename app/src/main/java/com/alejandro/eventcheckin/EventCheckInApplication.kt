package com.alejandro.eventcheckin

import android.app.Application
import com.alejandro.eventcheckin.data.AppDatabase
import com.alejandro.eventcheckin.data.EventRepository

/** Owns the database and the repository for the whole process. */
class EventCheckInApplication : Application() {

    val repository: EventRepository by lazy {
        val database = AppDatabase.getInstance(this)
        EventRepository(database.eventDao(), database.attendeeDao())
    }
}
