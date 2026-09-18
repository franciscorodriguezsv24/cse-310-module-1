# Demo + code walkthrough script

Target length: **5 minutes** (4:30 - 5:30 is safe). The rubric needs three
things in one video: **your face on camera**, a **demo of the app running**, and
a **detailed code walkthrough**. Keep the webcam bubble visible the whole time,
not just at the start.

## Before you hit record

```bash
# clean, predictable starting data (seeds the 3 sample events again)
~/Library/Android/sdk/platform-tools/adb shell pm clear com.alejandro.eventcheckin
~/Library/Android/sdk/platform-tools/adb shell settings put system user_rotation 0
./gradlew installDebug
```

- Android Studio: bump the editor font (Cmd+Shift+A -> "Increase font size" a few
  times) so the code is readable at 1080p.
- Open these files in tabs, in this order, so you never hunt for a file on camera:
  `Event.kt`, `AttendeeDao.kt`, `AppDatabase.kt`, `EventRepository.kt`,
  `EventViewModel.kt`, `EventCheckInApp.kt`, `EventListScreen.kt`,
  `AttendeeFormScreen.kt`, `Validation.kt`, `ValidationTest.kt`.
- Emulator window next to the editor, or record it separately and cut.
- Close Slack, mail and notifications.

---

## 0:00 - 0:25 | Intro (camera on you)

> Hi, I'm Alejandro Rodriguez. This is my CSE 310 Module 1 project. I chose the
> Kotlin language module, and I built a native Android app called Event
> Check-In. My mobile experience before this was all React Native, so Kotlin,
> Jetpack Compose and the Android SDK were all new to me. Let me show you the app
> running first, and then walk through the code.

---

## 0:25 - 2:20 | Demo (screen: emulator)

**1. The event list (0:25)**

> This is the event list. Each card is an event with its date and location, and
> on the right is the check-in count: two of three people are here for the Ward
> Activity.

**2. Create an event (0:40)** - tap the **+** button, tap **Save** with the form empty.

> The plus button opens the new event form. If I save it empty, every field tells
> me what is wrong instead of failing silently. That validation is shared between
> both forms.

Now fill it in: name `Temple Trip`, location `Provo`, date `Oct 3, 2026`, **Save**.

> With valid input it saves and the list updates by itself. I never told the list
> to refresh, and that will make sense when we get to the code.

**3. Attendees and check-in (1:10)** - open **Ward Activity**.

> Inside an event I see its attendees and a running count at the top. Tapping a
> checkbox checks someone in...

Tap an unchecked box.

> ...and the count goes from two of three to three of three immediately.

Tap the **+** button, add `Emily Chen` with phone `5550150`, **Save**.

> Adding a person takes a name and a phone, both validated, and the new attendee
> shows up right away.

**4. Search (1:40)** - go back, type `study` in the search box, then `zzz`.

> The search box filters events by name, location or date. And when nothing
> matches, the app says so instead of showing an empty screen. Every screen with
> nothing to draw has a message like this.

Clear the search.

**5. Rotation (1:55)** - open the add-attendee form, type a name, rotate the emulator.

> Rotation was the part with no React Native equivalent. Android destroys and
> recreates the screen, but what I typed is still here, because the form state is
> saved and the lists live in a ViewModel.

**6. Persistence (2:05)** - rotate back, go home, swipe the app away (or force-stop), reopen it.

> And the data is on the device. I am killing the app completely and reopening
> it, and Temple Trip and Emily Chen are both still here, because everything goes
> into a SQLite database through Room.

---

## 2:20 - 4:45 | Code walkthrough (screen: Android Studio)

**1. `data/Event.kt` and `data/Attendee.kt` (2:20)**

> The data layer starts with two Kotlin data classes. A data class gives me
> equals, toString and copy for free, and the Room annotations turn them into
> tables: `@Entity` names the table, `@PrimaryKey(autoGenerate = true)` lets
> SQLite assign the id. Attendee has a foreign key to Event with
> `onDelete = CASCADE`, so deleting an event deletes its attendees in the
> database instead of in my code.

**2. `data/AttendeeDao.kt` (2:40)**

> The DAO is an interface. I write the SQL, and KSP generates the implementation
> at build time, which means a typo in this query fails the build instead of
> crashing the app. Two things matter here. `observeAttendees` returns a `Flow`,
> so Room re-emits the whole list every time any row changes. And `setCheckedIn`
> updates only the check-in column, so checking someone in never rewrites their
> name.

**3. `data/AppDatabase.kt` (3:00)**

> The database lists its entities and its DAOs. `getInstance` keeps one instance
> for the whole process, and this callback seeds the three sample events the
> first time the app runs, which is what you saw when the app started.

**4. `data/EventRepository.kt` (3:15)**

> The repository is the only thing the rest of the app talks to. It exposes the
> two flows and a suspend function for every write. No screen ever touches a DAO.

**5. `ui/events/EventViewModel.kt` (3:25)** - this is the important one, spend time here.

> Here is where the state lives. `stateIn` turns each Room flow into a
> `StateFlow`, which always has a current value, so the UI has something to draw
> immediately and keeps it through a rotation.
>
> `filteredEvents` uses `combine`: it takes the events flow and the search text
> and produces the filtered list. When either one changes, the list recomputes
> itself. That is why the UI has no refresh call anywhere.
>
> Every write is a `viewModelScope.launch` calling the repository, so the
> database work happens off the main thread and the coroutine is cancelled if the
> ViewModel goes away.
>
> And this factory hands the ViewModel the repository that lives in the
> Application class.

**6. `ui/EventCheckInApp.kt` (3:55)**

> Navigation is a `NavHost` with one `composable` per destination. The routes are
> built here so no screen writes a path by hand, and the event id travels in the
> route as a typed `Long` argument. This piece was a bug fix: if a destination's
> row is missing, it waits while the database loads, and closes itself if the row
> is really gone, which is what happens right after you delete an event.

**7. `ui/events/EventListScreen.kt` (4:10)**

> The UI is declarative. `LazyColumn` is the recycling list, like FlatList in
> React Native: `items` with a stable `key` so rows keep their identity. This
> `when` block is the empty-state logic, and notice the screen takes its data and
> its callbacks as parameters, which is what lets me preview it with sample data
> without running the app.

**8. `ui/events/AttendeeFormScreen.kt` and `Validation.kt` (4:25)**

> The form keeps its fields in `rememberSaveable`, which is what survived the
> rotation in the demo. The rules themselves are in `Validation.kt`, plain
> functions returning a message or null, so the same rules serve both forms...

**9. `ValidationTest.kt` (4:35)** - run the tests on camera if you have time.

> ...and so I can unit test them. Seven JUnit tests cover the name, phone,
> location and date rules, and they pass.

---

## 4:45 - 5:00 | Close (camera on you)

> That is Event Check-In: Kotlin, Jetpack Compose, Navigation, a ViewModel with
> StateFlow, and Room for persistence. The biggest thing I learned is how one-way
> data flow works on Android: the database emits, the ViewModel holds, the UI
> collects, and writes go back through the repository. The repository link and
> the README are in the description. Thanks for watching.

---

## Notes

- If you run long, cut the rotation demo (1:55) and the `AppDatabase` stop (3:00).
- Say what the code *does*, not what it *is*: "this re-emits the list on every
  write" beats "this is a Flow".
- Post the YouTube link in Microsoft Teams and replace the placeholder in the
  README before you submit.
