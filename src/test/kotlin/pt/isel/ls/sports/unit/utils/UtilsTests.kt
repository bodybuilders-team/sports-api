package pt.isel.ls.sports.unit.utils

import pt.isel.ls.sports.api.utils.toIntOrThrow
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.utils.substringOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class UtilsTests {

    // substringOrNull

    @Test
    fun `substringOrNull returns a substring correctly`() {
        val str = "Vamos ter 20!"
        assertEquals(str.substring(5), str.substringOrNull(5))
    }

    @Test
    fun `substringOrNull returns null if startIndex is greater than length`() {
        val str = "Vamos ter 20!"
        assertNull(str.substringOrNull(20))
    }

    // toIntOrThrow

    @Test
    fun `toIntOrThrow returns a Int correctly`() {
        assertEquals(20, "20".toIntOrThrow())
    }

    @Test
    fun `toIntOrThrow throws SportsError badRequest if string is not a valid representation of a number`() {
        assertFailsWith<InvalidArgumentException> {
            "Hey".toIntOrThrow()
        }
    }
}
