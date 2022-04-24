package pt.isel.ls.sports.unit.domain

import pt.isel.ls.sports.domain.Sport
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SportTests {

    // Sport creation

    @Test
    fun `create a Sport with valid information`() {
        val sport = Sport(id = 1, name = "Badminton", uid = 1, description = "")
        assertEquals(1, sport.id)
        assertEquals("Badminton", sport.name)
        assertEquals(1, sport.uid)
        assertEquals("", sport.description)
    }

    @Test
    fun `create a Sport with invalid ID throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Sport(id = -1, name = "Badminton", uid = 1, description = "")
        }
    }

    @Test
    fun `create a Sport with invalid name throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Sport(id = 1, name = "", uid = 1, description = "")
        }
    }

    @Test
    fun `create a Sport with invalid description throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Sport(id = 1, name = "Badminton", uid = 1, description = "a".repeat(Sport.MAX_DESCRIPTION_LENGTH + 1))
        }
    }

    @Test
    fun `create a Sport with invalid uid throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Sport(id = 1, name = "Badminton", uid = -1, description = "")
        }
    }

    // isValidName

    @Test
    fun `isValidName returns true with a string with a valid length`() {
        assertTrue { Sport.isValidName("a".repeat(Sport.MIN_NAME_LENGTH)) }
    }

    @Test
    fun `isValidName returns false with string shorter than min length`() {
        assertFalse { Sport.isValidName("a".repeat(Sport.MIN_NAME_LENGTH - 1)) }
    }

    @Test
    fun `isValidName returns false with string longer than max length`() {
        assertFalse { Sport.isValidName("a".repeat(Sport.MAX_NAME_LENGTH + 1)) }
    }

    // isValidDescription

    @Test
    fun `isValidDescription returns true with a string with a valid length`() {
        assertTrue { Sport.isValidDescription("a".repeat(Sport.MIN_DESCRIPTION_LENGTH)) }
    }

    @Test
    fun `isValidDescription returns false with string longer than max length`() {
        assertFalse { Sport.isValidDescription("a".repeat(Sport.MAX_DESCRIPTION_LENGTH + 1)) }
    }
}
