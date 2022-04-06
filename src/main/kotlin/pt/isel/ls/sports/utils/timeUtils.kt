package pt.isel.ls.sports.utils

import kotlin.time.Duration

const val HOURS_INDEX = 0
const val MINUTES_INDEX = 1
const val SECONDS_INDEX = 2

/**
 * Converts a string in the format "hh:mm:ss.fff" to a Duration.
 * @return duration
 */
fun String.toDuration(): Duration {
    val dateParts = split(":")
    return Duration.parse("PT${dateParts[HOURS_INDEX]}H${dateParts[MINUTES_INDEX]}M${dateParts[SECONDS_INDEX]}S")
}

/**
 * Converts a duration to a String in the format "hh:mm:ss.fff".
 * @return string
 */
fun Duration.toDTOString(): String =
    toComponents { hours, minutes, seconds, nanoseconds ->
        "${hours.toString().padStart(2, '0')}:" +
            "${minutes.toString().padStart(2, '0')}:" +
            "${seconds.toString().padStart(2, '0')}.${nanoseconds.toString().padStart(3, '0').substring(0..2)}"
    }
