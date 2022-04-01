package pt.isel.ls.sports.domain

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.services.utils.isValidId

/**
 * Activity representation.
 *
 * @property id activity unique identifier
 * @property date activity date
 * @property duration duration of the activity
 * @property uid unique identifier of the user who created the activity
 * @property sid unique identifier of the activity sport
 * @property rid unique identifier of the activity route (optional)
 */
@Serializable
data class Activity(
    val id: Int,
    val date: String,
    val duration: String,
    val uid: Int,
    val sid: Int,
    val rid: Int? = null
) {
    companion object {
        private const val DATE_REGEX = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$"
        private const val DURATION_REGEX = "^(?:[01]\\d|2[0123])\\:(?:[012345]\\d)\\:(?:[012345]\\d)\\.\\d{3}$"

        /**
         * Checks if a date e valid.
         * @param date date to check
         * @return true if its valid
         */
        fun isValidDate(date: String): Boolean =
            date.matches(DATE_REGEX.toRegex())

        /**
         * Checks if a duration e valid.
         * @param duration duration to check
         * @return true if its valid
         */
        fun isValidDuration(duration: String): Boolean =
            duration.matches(DURATION_REGEX.toRegex())
    }

    init {
        require(isValidId(id)) { "Invalid activity id: $id" }
        require(isValidDate(date)) { "Invalid activity date: $date" }
        require(isValidDuration(duration)) { "Invalid activity duration: $duration" }
        require(isValidId(uid)) { "Invalid activity user id: $uid" }
        require(isValidId(sid)) { "Invalid activity sport id: $sid" }
        if (rid != null) require(isValidId(rid)) { "Invalid activity route id: $rid" }
    }
}
