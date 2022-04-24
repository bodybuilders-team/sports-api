package pt.isel.ls.sports.unit.domain

import kotlinx.datetime.toLocalDate
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.utils.toDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ActivityTests {

    // Activity creation

    @Test
    fun `create a Activity with valid information`() {
        val activity = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        assertEquals(1, activity.id)
        assertEquals("2022-11-20".toLocalDate(), activity.date)
        assertEquals("20:23:55.263".toDuration(), activity.duration)
        assertEquals(1, activity.uid)
        assertEquals(1, activity.sid)
        assertEquals(1, activity.rid)
    }

    @Test
    fun `create a Activity with invalid ID throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Activity(-1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        }
    }

    @Test
    fun `create a Activity with invalid uid throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), -1, 1, 1)
        }
    }

    @Test
    fun `create a Activity with invalid sid throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, -1, 1)
        }
    }

    @Test
    fun `create a Activity with invalid rid throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, -1)
        }
    }
}
