package pt.isel.ls.sports.unit.services.utils

import pt.isel.ls.sports.services.utils.isValidId
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ServicesUtilsTests {

    // isValidId

    @Test
    fun `isValidId returns true if the ID is positive`() {
        assertTrue(isValidId(1))
    }

    @Test
    fun `isValidId returns true if the ID is zero`() {
        assertTrue(isValidId(0))
    }

    @Test
    fun `isValidId returns false if the ID is negative`() {
        assertFalse(isValidId(-1))
    }
}
