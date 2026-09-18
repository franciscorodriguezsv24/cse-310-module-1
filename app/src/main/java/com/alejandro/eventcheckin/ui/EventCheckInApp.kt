package com.alejandro.eventcheckin.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alejandro.eventcheckin.ui.events.AttendeeFormScreen
import com.alejandro.eventcheckin.ui.events.EventDetailScreen
import com.alejandro.eventcheckin.ui.events.EventFormScreen
import com.alejandro.eventcheckin.ui.events.EventListScreen
import com.alejandro.eventcheckin.ui.events.EventViewModel

/** Route names kept in one place so a typo cannot go unnoticed. */
object Routes {
    const val EVENT_LIST = "events"
    const val NEW_EVENT = "events/new"
    const val EVENT_DETAIL = "events/{eventId}"
    const val EDIT_EVENT = "events/{eventId}/edit"
    const val ADD_ATTENDEE = "events/{eventId}/attendees/new"
    const val EDIT_ATTENDEE = "events/{eventId}/attendees/{attendeeId}"

    fun eventDetail(eventId: Long) = "events/$eventId"
    fun editEvent(eventId: Long) = "events/$eventId/edit"
    fun addAttendee(eventId: Long) = "events/$eventId/attendees/new"
    fun editAttendee(eventId: Long, attendeeId: Long) = "events/$eventId/attendees/$attendeeId"
}

/** The navigation graph: list -> detail -> forms. */
@Composable
fun EventCheckInApp(viewModel: EventViewModel = viewModel(factory = EventViewModel.Factory)) {
    val navController = rememberNavController()
    val events by viewModel.events.collectAsStateWithLifecycle()
    val filteredEvents by viewModel.filteredEvents.collectAsStateWithLifecycle()
    val attendees by viewModel.attendees.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = Routes.EVENT_LIST) {

        composable(Routes.EVENT_LIST) {
            EventListScreen(
                events = filteredEvents,
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                checkedInCount = { id -> attendees.count { it.eventId == id && it.checkedIn } },
                totalCount = { id -> attendees.count { it.eventId == id } },
                onEventClick = { event -> navController.navigate(Routes.eventDetail(event.id)) },
                onAddEvent = { navController.navigate(Routes.NEW_EVENT) },
                hasAnyEvent = events.isNotEmpty()
            )
        }

        composable(Routes.NEW_EVENT) {
            EventFormScreen(
                title = "New event",
                event = null,
                onSave = { name, location, date ->
                    viewModel.addEvent(name, location, date)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.EVENT_DETAIL,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: return@composable
            val event = events.find { it.id == eventId }
            if (event == null) {
                // The event was deleted, or the list has not loaded yet after a
                // process restart. Wait for the data instead of drawing a blank screen.
                LoadingOrGone(isLoading = events.isEmpty(), onGone = { navController.popBackStack() })
                return@composable
            }
            EventDetailScreen(
                event = event,
                attendees = attendees.filter { it.eventId == eventId },
                onToggleCheckIn = viewModel::toggleCheckIn,
                onAttendeeClick = { attendee ->
                    navController.navigate(Routes.editAttendee(eventId, attendee.id))
                },
                onAddAttendee = { navController.navigate(Routes.addAttendee(eventId)) },
                onEditEvent = { navController.navigate(Routes.editEvent(eventId)) },
                onDeleteEvent = {
                    viewModel.deleteEvent(event)
                    navController.popBackStack(Routes.EVENT_LIST, inclusive = false)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.EDIT_EVENT,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: return@composable
            val event = events.find { it.id == eventId }
            if (event == null) {
                LoadingOrGone(isLoading = events.isEmpty(), onGone = { navController.popBackStack() })
                return@composable
            }
            EventFormScreen(
                title = "Edit event",
                event = event,
                onSave = { name, location, date ->
                    viewModel.updateEvent(event, name, location, date)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.ADD_ATTENDEE,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: return@composable
            val event = events.find { it.id == eventId }
            if (event == null) {
                LoadingOrGone(isLoading = events.isEmpty(), onGone = { navController.popBackStack() })
                return@composable
            }
            AttendeeFormScreen(
                title = "Add attendee",
                eventName = event.name,
                attendee = null,
                onSave = { name, phone ->
                    viewModel.addAttendee(eventId, name, phone)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.EDIT_ATTENDEE,
            arguments = listOf(
                navArgument("eventId") { type = NavType.LongType },
                navArgument("attendeeId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: return@composable
            val attendeeId = backStackEntry.arguments?.getLong("attendeeId") ?: return@composable
            val event = events.find { it.id == eventId }
            val attendee = attendees.find { it.id == attendeeId }
            if (event == null || attendee == null) {
                LoadingOrGone(
                    isLoading = events.isEmpty() || attendees.isEmpty(),
                    onGone = { navController.popBackStack() }
                )
                return@composable
            }
            AttendeeFormScreen(
                title = "Edit attendee",
                eventName = event.name,
                attendee = attendee,
                onSave = { name, phone ->
                    viewModel.updateAttendee(attendee, name, phone)
                    navController.popBackStack()
                },
                onDelete = {
                    viewModel.deleteAttendee(attendee)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Drawn while a destination has no data. Either the database has not emitted
 * yet, in which case a spinner is enough, or the row is really gone and the
 * screen closes itself instead of leaving the user on an empty screen.
 */
@Composable
private fun LoadingOrGone(isLoading: Boolean, onGone: () -> Unit) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LaunchedEffect(Unit) { onGone() }
    }
}
