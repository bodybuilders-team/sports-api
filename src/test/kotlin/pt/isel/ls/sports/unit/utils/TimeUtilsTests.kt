package pt.isel.ls.sports.unit.utils

import pt.isel.ls.sports.utils.toDTOString
import pt.isel.ls.sports.utils.toDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration

class TimeUtilsTests {

    // toDuration

    @Test
    fun `toDuration converts a String into a Duration correctly`() {
        val duration = "10:11:69.420".toDuration()
        assertEquals(Duration.parse("PT10H12M9.420S"), duration)
    }

    @Test
    fun `toDuration converts a String into a Duration ZERO correctly`() {
        val duration = "00:00:00.000".toDuration()
        assertEquals(Duration.ZERO, duration)
    }

    // toDTOString

    @Test
    fun `toDTOString converts a Duration into a String correctly`() {
        val duration = Duration.parse("PT10H12M9.000S").toDTOString()
        assertEquals("10:12:09.000", duration)
    }

    @Test
    fun `toDTOString converts a Duration ZERO into a String correctly`() {
        val duration = Duration.ZERO.toDTOString()
        assertEquals("00:00:00.000", duration)
    }
}
