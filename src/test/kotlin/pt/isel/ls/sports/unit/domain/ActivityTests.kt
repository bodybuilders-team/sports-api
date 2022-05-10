package pt.isel.ls.sports.unit.domain

import kotlinx.datetime.toLocalDate
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.utils.toDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ActivityTests {

    // Activity object instantiation

    @Test
    fun `Instantiate an Activity object with valid information`() {
        val activity =
            Activity(id = 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid = 1, sid = 1, rid = 1)
        assertEquals(1, activity.id)
        assertEquals("2022-11-20".toLocalDate(), activity.date)
        assertEquals("20:23:55.263".toDuration(), activity.duration)
        assertEquals(1, activity.uid)
        assertEquals(1, activity.sid)
        assertEquals(1, activity.rid)
    }

    @Test
    fun `Instantiate an Activity object without route`() {
        val activity = Activity(id = 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid = 1, sid = 1)
        assertEquals(1, activity.id)
        assertEquals("2022-11-20".toLocalDate(), activity.date)
        assertEquals("20:23:55.263".toDuration(), activity.duration)
        assertEquals(1, activity.uid)
        assertEquals(1, activity.sid)
    }

    @Test
    fun `Instantiate an Activity object with invalid ID throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Activity(id = -1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid = 1, sid = 1, rid = 1)
        }
    }

    @Test
    fun `Instantiate an Activity object with invalid uid throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Activity(id = 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid = -1, sid = 1, rid = 1)
        }
    }

    @Test
    fun `Instantiate an Activity object with invalid sid throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Activity(id = 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid = 1, sid = -1, rid = 1)
        }
    }

    @Test
    fun `Instantiate an Activity object with invalid rid throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            Activity(id = 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid = 1, sid = 1, rid = -1)
        }
    }
}
