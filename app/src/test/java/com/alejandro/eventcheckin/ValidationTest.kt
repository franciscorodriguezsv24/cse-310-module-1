package com.alejandro.eventcheckin

import com.alejandro.eventcheckin.ui.events.Validation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Unit tests for the form rules, so the forms can be trusted without tapping through them. */
class ValidationTest {

    @Test
    fun `a blank name is rejected`() {
        assertEquals(Validation.NAME_REQUIRED, Validation.nameError("   "))
    }

    @Test
    fun `a one letter name is rejected`() {
        assertEquals(Validation.NAME_TOO_SHORT, Validation.nameError("A"))
    }

    @Test
    fun `a normal name is accepted`() {
        assertNull(Validation.nameError("Maria Lopez"))
    }

    @Test
    fun `a blank phone is rejected`() {
        assertEquals(Validation.PHONE_REQUIRED, Validation.phoneError(""))
    }

    @Test
    fun `a phone with too few digits is rejected`() {
        assertEquals(Validation.PHONE_INVALID, Validation.phoneError("555"))
    }

    @Test
    fun `a phone with separators is accepted`() {
        assertNull(Validation.phoneError("(555) 010-0199"))
    }

    @Test
    fun `location and date are required`() {
        assertEquals(Validation.LOCATION_REQUIRED, Validation.locationError(""))
        assertEquals(Validation.DATE_REQUIRED, Validation.dateError(" "))
        assertNull(Validation.locationError("Cultural Hall"))
        assertNull(Validation.dateError("Sep 20, 2026"))
    }
}
