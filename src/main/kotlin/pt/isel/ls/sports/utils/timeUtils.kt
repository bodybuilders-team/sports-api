package pt.isel.ls.sports.utils

import kotlin.time.Duration

private const val HOURS_INDEX = 0
private const val MINUTES_INDEX = 1
private const val SECONDS_INDEX = 2

private const val HOURS_LEN = 2
private const val MINUTES_LEN = 2
private const val SECONDS_LEN = 2
private const val NANOS_LEN = 3

/**
 * Converts a string in the format "hh:mm:ss.fff" to a [Duration].
 *
 * @return converted duration
 */
fun String.toDuration(): Duration {
    val dateParts = split(":")
    return Duration.parse("PT${dateParts[HOURS_INDEX]}H${dateParts[MINUTES_INDEX]}M${dateParts[SECONDS_INDEX]}S")
}

/**
 * Converts a duration to a [String] in the format "hh:mm:ss.fff".
 *
 * @return converted string
 */
fun Duration.toDTOString(): String =
    toComponents { hours, minutes, seconds, nanoseconds ->
        "${hours.toString().padStart(HOURS_LEN, '0')}:" +
                "${minutes.toString().padStart(MINUTES_LEN, '0')}:" +
                "${seconds.toString().padStart(SECONDS_LEN, '0')}." +
                nanoseconds.toString().padStart(NANOS_LEN, '0').substring(0 until NANOS_LEN)
    }
