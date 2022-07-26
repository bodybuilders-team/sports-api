package pt.isel.ls.sports.unit.domain

import pt.isel.ls.sports.domain.Route
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RouteTests {

    // Route object instantiation

    @Test
    fun `Instantiate a Route object with valid information`() {
        val route = Route(id = 1, startLocation = "Odivelas", endLocation = "Chelas", distance = 10.0, uid = 1)
        assertEquals(1, route.id)
        assertEquals("Odivelas", route.startLocation)
        assertEquals("Chelas", route.endLocation)
        assertEquals(10.0, route.distance)
        assertEquals(1, route.uid)
    }

    @Test
    fun `Instantiate a Route object with invalid ID throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Route(id = -1, startLocation = "Odivelas", endLocation = "Chelas", distance = 10.0, uid = 1)
        }
    }

    @Test
    fun `Instantiate a Route object with invalid distance throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Route(id = 1, startLocation = "Odivelas", endLocation = "Chelas", distance = -10.0, uid = 1)
        }
    }

    @Test
    fun `Instantiate a Route object with invalid uid throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Route(id = 1, startLocation = "Odivelas", endLocation = "Chelas", distance = 10.0, uid = -1)
        }
    }

    // isValidLocation

    @Test
    fun `isValidLocation returns true with a string with a valid length`() {
        assertTrue { Route.isValidLocation("a".repeat((Route.MIN_LOCATION_LENGTH..Route.MAX_LOCATION_LENGTH).random())) }
    }

    @Test
    fun `isValidLocation returns true with a string at min length`() {
        assertTrue { Route.isValidLocation("a".repeat(Route.MIN_LOCATION_LENGTH)) }
    }

    @Test
    fun `isValidLocation returns true with a string at max length`() {
        assertTrue { Route.isValidLocation("a".repeat(Route.MAX_LOCATION_LENGTH)) }
    }

    @Test
    fun `isValidLocation returns false with string shorter than min length`() {
        assertFalse { Route.isValidLocation("a".repeat(Route.MIN_LOCATION_LENGTH - 1)) }
    }

    @Test
    fun `isValidLocation returns false with string longer than max length`() {
        assertFalse { Route.isValidLocation("a".repeat(Route.MAX_LOCATION_LENGTH + 1)) }
    }

    // isValidDistance

    @Test
    fun `isValidDistance returns true with a positive distance`() {
        assertTrue { Route.isValidDistance(10.0) }
    }

    @Test
    fun `isValidDistance returns false with a negative distance`() {
        assertFalse { Route.isValidDistance(-10.0) }
    }

    @Test
    fun `isValidDistance returns true with a zero distance`() {
        assertTrue { Route.isValidDistance(0.0) }
    }
}
