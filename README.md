# Event Check-In

CSE 310 - Applied Programming, Module 1: **Language - Kotlin**
Francisco Alejandro Rodriguez Bonilla

A native Android app for running the door at an event. An organizer creates an
event, registers the people who are coming, marks them as checked in, and sees a
live count for each event. Everything is stored on the device, so the list is
still there the next morning.

My mobile experience so far is in React Native, so both Kotlin and the Android
SDK were new to me for this module. The app is written entirely in Kotlin with
Jetpack Compose and has no JavaScript in it.

## Screenshots

| Event list | Event detail | Attendee form |
| --- | --- | --- |
| ![Event list](docs/screenshots/event-list.png) | ![Event detail](docs/screenshots/event-detail.png) | ![Attendee form](docs/screenshots/attendee-form.png) |

## What the app does

- Create, edit and delete events (name, location, date). Deleting an event asks
  for confirmation and removes its attendees with it.
- Register attendees for an event, edit them, and delete them.
- Tap the checkbox to check a person in. Every screen that shows a count updates
  itself right away.
- Search the event list by name, location or date.
- Everything is saved in a local SQLite database through Room and survives
  closing the app.

## Software demo video

Demo and code walkthrough: <https://youtu.be/REPLACE_WITH_VIDEO_ID>

## Development environment

- Android Studio with the Android SDK, compile and target SDK 35, minimum SDK 26
- Kotlin 2.1.20, Gradle 8.14.3, Android Gradle Plugin 8.11.0, JDK 21
- Jetpack Compose (Compose BOM 2025.05.01) with Material 3
- Navigation Compose, Lifecycle ViewModel, Kotlin coroutines and Flow
- Room 2.7.1 with KSP for the generated database code
- JUnit 4 for the unit tests
- Tested on a Pixel 8 emulator running API 35

## How to run it

```bash
git clone <this repo>
cd cse-310-module-1
./gradlew assembleDebug                 # build the debug APK
./gradlew installDebug                  # install it on a running emulator or device
./gradlew testDebugUnitTest             # run the unit tests
```

Opening the folder in Android Studio and pressing Run works too. Android Studio
writes `local.properties` with the path to your SDK the first time it syncs; that
file is not in the repository because it is specific to each machine.

## How the code is organized

```
app/src/main/java/com/alejandro/eventcheckin/
├── EventCheckInApplication.kt      owns the database and the repository
├── MainActivity.kt                 hosts the Compose UI
├── data/
│   ├── Event.kt, Attendee.kt       Room entities
│   ├── EventDao.kt, AttendeeDao.kt queries returning Flow
│   ├── AppDatabase.kt              the database, seeded on first run
│   ├── EventRepository.kt          the only thing the ViewModel talks to
│   └── SampleData.kt               seed rows and preview data
└── ui/
    ├── EventCheckInApp.kt          the navigation graph
    ├── events/
    │   ├── EventViewModel.kt       StateFlow state and every write
    │   ├── EventListScreen.kt      list, search, empty states
    │   ├── EventDetailScreen.kt    attendees, check-in, delete dialog
    │   ├── EventFormScreen.kt      create and edit an event
    │   ├── AttendeeFormScreen.kt   create and edit an attendee
    │   └── Validation.kt           the input rules
    └── theme/                      Material 3 colors and typography

kotlin-basics/KotlinBasics.kt       the language exercises from the first week
```

The data flows one way: Room returns a `Flow`, the ViewModel turns it into a
`StateFlow` with `stateIn`, the composables collect it with
`collectAsStateWithLifecycle`, and every write goes back through the repository.
No composable touches a DAO.

## Module requirements

| Requirement | Where it is met |
| --- | --- |
| Written in a new language (Kotlin) | the whole `app` module and `kotlin-basics/` |
| Mobile app on the platform framework | Jetpack Compose with Material 3, `@Preview` composables |
| Multiple screens with navigation | `ui/EventCheckInApp.kt`, list -> detail -> forms |
| User input with validation | `EventFormScreen`, `AttendeeFormScreen`, `Validation.kt` |
| Displays a collection of data | `LazyColumn` in `EventListScreen` and `EventDetailScreen` |
| Local data persistence | Room entities, DAOs and `AppDatabase` |
| Published to GitHub with a README | this file |

## What I learned

- Compose is closer to React than I expected. A composable is a function of its
  state, and `remember` and `rememberSaveable` map onto `useState`, except that
  `rememberSaveable` also survives the activity being recreated on rotation.
- Rotation is the part that has no React Native equivalent. Anything that lives
  only in a composable is gone after a rotation unless it is saved, which is why
  the lists live in the ViewModel and the form fields use `rememberSaveable`.
- `Flow` plus `stateIn` means the UI never asks the database for anything. Room
  emits a new list after every write and the count on the list screen updates by
  itself, with no refresh call anywhere.
- Kotlin's null safety changes how the code reads. `?.`, `?:` and the smart casts
  replace the defensive checks I write in JavaScript, and the compiler catches
  the cases I would have missed.
- `data class` with `copy` makes immutable updates natural, so the ViewModel
  never mutates a row in place.
- KSP generates the Room implementation at build time, so a bad SQL string in a
  `@Query` fails the build instead of crashing at runtime.

## Future work

Ideas that were deliberately left out to keep the sprint scoped:

- QR code scanning to check someone in without looking them up.
- Cloud sync and sign in, so two people can staff the same door.
- Export the attendee list to CSV.
- A real date picker instead of a free text date field.
- Instrumented UI tests with Compose testing, on top of the current unit tests.
