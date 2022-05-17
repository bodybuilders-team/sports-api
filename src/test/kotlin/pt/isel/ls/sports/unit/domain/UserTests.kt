package pt.isel.ls.sports.unit.domain

import pt.isel.ls.sports.domain.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserTests {

    // User object instantiation

    @Test
    fun `Instantiate a User object with valid information`() {
        val user = User(id = 1, name = "Paulão", email = "paulao@isel.com", password = "H42xS")
        assertEquals(1, user.id)
        assertEquals("Paulão", user.name)
        assertEquals("paulao@isel.com", user.email)
    }

    @Test
    fun `Instantiate a User object with invalid ID throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            User(id = -1, name = "Paulão", email = "paulao@isel.com", password = "H42xS")
        }
    }

    @Test
    fun `Instantiate a User object with invalid name throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            User(id = 1, name = "", email = "paulao@isel.com", password = "H42xS")
        }
    }

    @Test
    fun `Instantiate a User object with invalid email throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            User(id = 1, name = "Paulão", email = "", password = "H42xS")
        }
    }

    // isValidName

    @Test
    fun `isValidName returns true with a string with a valid length`() {
        assertTrue { User.isValidName("a".repeat((User.MIN_NAME_LENGTH..User.MAX_NAME_LENGTH).random())) }
    }

    @Test
    fun `isValidName returns true with a string at min length`() {
        assertTrue { User.isValidName("a".repeat(User.MIN_NAME_LENGTH)) }
    }

    @Test
    fun `isValidName returns true with a string at max length`() {
        assertTrue { User.isValidName("a".repeat(User.MAX_NAME_LENGTH)) }
    }

    @Test
    fun `isValidName returns false with string shorter than min length`() {
        assertFalse { User.isValidName("a".repeat(User.MIN_NAME_LENGTH - 1)) }
    }

    @Test
    fun `isValidName returns false with string longer than max length`() {
        assertFalse { User.isValidName("a".repeat(User.MAX_NAME_LENGTH + 1)) }
    }

    // isValidEmail

    @Test
    fun `isValidEmail returns true with a string that matches the regular expression`() {
        assertTrue { User.isValidEmail("abc@.") }
    }

    @Test
    fun `isValidEmail returns true with a string that does not match the regular expression`() {
        assertFalse { User.isValidEmail("123") }
    }

    @Test
    fun `isValidEmail returns false with a string without the char @`() {
        assertFalse { User.isValidEmail("abc.") }
    }

    @Test
    fun `isValidEmail returns false with a string without a dot`() {
        assertFalse { User.isValidEmail("abc@") }
    }
}
