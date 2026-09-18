package com.alejandro.eventcheckin.ui.events

/** Input rules shared by the two forms, kept out of the composables so they stay readable. */
object Validation {

    const val NAME_REQUIRED = "Name is required"
    const val NAME_TOO_SHORT = "Use at least 2 characters"
    const val LOCATION_REQUIRED = "Location is required"
    const val DATE_REQUIRED = "Date is required"
    const val PHONE_REQUIRED = "Phone is required"
    const val PHONE_INVALID = "Use 7 to 15 digits"

    fun nameError(value: String): String? = when {
        value.isBlank() -> NAME_REQUIRED
        value.trim().length < 2 -> NAME_TOO_SHORT
        else -> null
    }

    fun locationError(value: String): String? =
        if (value.isBlank()) LOCATION_REQUIRED else null

    fun dateError(value: String): String? =
        if (value.isBlank()) DATE_REQUIRED else null

    fun phoneError(value: String): String? {
        val digits = value.count { it.isDigit() }
        return when {
            value.isBlank() -> PHONE_REQUIRED
            digits < 7 || digits > 15 -> PHONE_INVALID
            else -> null
        }
    }
}
