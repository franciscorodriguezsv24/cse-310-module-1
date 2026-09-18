package com.alejandro.eventcheckin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** The SQLite database. Room generates the implementation from these annotations. */
@Database(entities = [Event::class, Attendee::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun attendeeDao(): AttendeeDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        /** Returns the one database for the process, building it the first time. */
        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: build(context).also { instance = it } }

        /** Builds the database file and attaches the callback that seeds it. */
        private fun build(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "event_checkin.db"
            )
                .addCallback(SeedCallback)
                .build()

        /** Puts the sample events in the database the first time the app runs. */
        private object SeedCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val database = instance ?: return
                CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                    val eventDao = database.eventDao()
                    val attendeeDao = database.attendeeDao()
                    SampleData.events.forEach { event ->
                        val eventId = eventDao.insert(event.copy(id = 0))
                        SampleData.attendeesFor(event.id).forEach { attendee ->
                            attendeeDao.insert(attendee.copy(id = 0, eventId = eventId))
                        }
                    }
                }
            }
        }
    }
}
