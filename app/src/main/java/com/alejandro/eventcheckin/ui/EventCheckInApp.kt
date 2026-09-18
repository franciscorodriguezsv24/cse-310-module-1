package com.alejandro.eventcheckin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alejandro.eventcheckin.ui.events.AddAttendeeScreen
import com.alejandro.eventcheckin.ui.events.EventDetailScreen
import com.alejandro.eventcheckin.ui.events.EventListScreen
import com.alejandro.eventcheckin.ui.events.EventViewModel

/** Route names kept in one place so a typo cannot go unnoticed. */
object Routes {
    const val EVENT_LIST = "events"
    const val EVENT_DETAIL = "events/{eventId}"
    const val ADD_ATTENDEE = "events/{eventId}/add"

    fun eventDetail(eventId: Long) = "events/$eventId"
    fun addAttendee(eventId: Long) = "events/$eventId/add"
}

/** The navigation graph: list -> detail -> add attendee. */
@Composable
fun EventCheckInApp(viewModel: EventViewModel = viewModel(factory = EventViewModel.Factory)) {
    val navController = rememberNavController()
    val events by viewModel.events.collectAsStateWithLifecycle()
    val attendees by viewModel.attendees.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = Routes.EVENT_LIST) {

        composable(Routes.EVENT_LIST) {
            EventListScreen(
                events = events,
                checkedInCount = { id -> attendees.count { it.eventId == id && it.checkedIn } },
                totalCount = { id -> attendees.count { it.eventId == id } },
                onEventClick = { event -> navController.navigate(Routes.eventDetail(event.id)) }
            )
        }

        composable(
            route = Routes.EVENT_DETAIL,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: return@composable
            val event = events.find { it.id == eventId } ?: return@composable
            EventDetailScreen(
                event = event,
                attendees = attendees.filter { it.eventId == eventId },
                onToggleCheckIn = { attendee -> viewModel.toggleCheckIn(attendee) },
                onAddAttendee = { navController.navigate(Routes.addAttendee(eventId)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.ADD_ATTENDEE,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: return@composable
            val event = events.find { it.id == eventId } ?: return@composable
            AddAttendeeScreen(
                eventName = event.name,
                onSave = { name, phone ->
                    viewModel.addAttendee(eventId, name, phone)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
