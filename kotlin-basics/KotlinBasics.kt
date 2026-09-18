/*
 * CSE 310 - Module 1 - Kotlin
 * Practice exercises written while reading the Kotlin documentation.
 * These are notes to myself about the language features I need for the app,
 * coming from JavaScript / React Native.
 *
 * Run it with:  kotlinc KotlinBasics.kt -include-runtime -d basics.jar && java -jar basics.jar
 */

// ---------------------------------------------------------------- variables
private fun variables() {
    val eventName = "Ward Activity"   // val  -> read only, like const
    var attendeeCount = 0             // var  -> reassignable, like let
    attendeeCount += 3

    val explicitType: Double = 4.5    // types are inferred, but can be written out
    val template = "$eventName has $attendeeCount attendees (${explicitType.toInt()} hours)"

    println(template)
}

// -------------------------------------------------------------- null safety
private fun nullSafety() {
    val phone: String? = null                 // the ? is what makes null legal at all
    println(phone?.length)                    // safe call -> prints null, does not crash
    println(phone?.length ?: 0)               // elvis operator -> default value

    val name: String? = "Alejandro"
    if (name != null) {
        println(name.length)                  // smart cast: no ? needed inside the check
    }
    println(name!!.uppercase())               // !! asserts non-null, throws if wrong
}

// ------------------------------------------------------------- data classes
// equals, hashCode, toString and copy are generated for me.
/** The shape I ended up using in the app, written here first as practice. */
data class Attendee(
    val id: Int,
    val name: String,
    val phone: String,
    val checkedIn: Boolean = false            // default argument
)

/** What a data class gives me for free. */
private fun dataClasses() {
    val attendee = Attendee(id = 1, name = "Maria", phone = "555-0100")
    val checkedIn = attendee.copy(checkedIn = true)   // copy instead of mutating

    println(attendee)
    println(checkedIn)
    println("same person? ${attendee.id == checkedIn.id}")
    println("equal objects? ${attendee == checkedIn}")

    val (id, name) = attendee                 // destructuring
    println("destructured: $id $name")
}

// -------------------------------------------------------------- collections
private fun collections() {
    val attendees = listOf(
        Attendee(1, "Maria", "555-0100", checkedIn = true),
        Attendee(2, "John", "555-0101"),
        Attendee(3, "Sara", "555-0102", checkedIn = true)
    )

    val names = attendees.map { it.name }                     // like Array.map
    val present = attendees.filter { it.checkedIn }           // like Array.filter
    val total = attendees.count { it.checkedIn }
    val byName = attendees.sortedBy { it.name }
    val found = attendees.find { it.phone == "555-0101" }

    println("names: $names")
    println("present: ${present.map { it.name }}")
    println("checked in: $total of ${attendees.size}")
    println("sorted: ${byName.map { it.name }}")
    println("found: ${found?.name}")

    val mutable = attendees.toMutableList()                   // lists are read only by default
    mutable.add(Attendee(4, "Luis", "555-0103"))
    println("after add: ${mutable.size}")

    val grouped: Map<Boolean, List<Attendee>> = attendees.groupBy { it.checkedIn }
    println("grouped keys: ${grouped.keys}")
}

// ---------------------------------------------------------------- functions
private fun greet(name: String, greeting: String = "Welcome"): String = "$greeting, $name!"

/** Extension function: adds a method to a type I did not write. */
private fun Attendee.initials(): String =                     // extension function
    name.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")

/** Takes a function as a parameter, the way map and filter do. */
private fun summarize(attendees: List<Attendee>, transform: (Attendee) -> String): String =
    attendees.joinToString(", ", transform = transform)       // function as a parameter

/** Default arguments, named arguments, extensions and lambdas. */
private fun functions() {
    println(greet("Alejandro"))
    println(greet("Bishop", greeting = "Hello"))              // named argument
    println(Attendee(1, "Maria Lopez", "555-0100").initials())
    println(summarize(listOf(Attendee(1, "Maria", "555-0100"))) { it.name })
}

// ------------------------------------------------------------- conditionals
/** when used without a subject, as a replacement for an if/else chain. */
private fun statusLabel(attendee: Attendee?): String = when {
    attendee == null -> "No attendee selected"
    attendee.checkedIn -> "Checked in"
    attendee.phone.isBlank() -> "Missing phone"
    else -> "Not here yet"
}

/** when used with a subject and ranges. */
private fun sizeLabel(count: Int): String = when (count) {    // when as an expression
    0 -> "Empty"
    in 1..9 -> "Small"
    in 10..49 -> "Medium"
    else -> "Large"
}

/** if and when are expressions in Kotlin, so they return a value. */
private fun conditionals() {
    val status = if (2 > 1) "if is an expression here" else "never"   // if returns a value
    println(status)
    println(statusLabel(null))
    println(statusLabel(Attendee(1, "Maria", "555-0100", checkedIn = true)))
    println(sizeLabel(0))
    println(sizeLabel(12))

    for (i in 1..3) print("$i ")                              // ranges
    println()
    repeat(3) { print("x") }
    println()
}

/** Runs every exercise in order. */
fun main() {
    variables()
    nullSafety()
    dataClasses()
    collections()
    functions()
    conditionals()
}
