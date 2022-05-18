package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.services.utils.isValidId
import pt.isel.ls.sports.utils.toDTOString

/**
 * Activity data transfer object representation.
 *
 * @property id activity's unique identifier
 * @property date date of the activity in string representation
 * @property duration duration of the activity in string representation
 * @property uid unique identifier of the user who created the activity
 * @property sid unique identifier of the activity sport
 * @property rid unique identifier of the activity route (optional)
 */
@Serializable
data class ActivityDTO(
    val id: Int,
    val date: String,
    val duration: String,
    val uid: Int,
    val sid: Int,
    val rid: Int? = null
) {
    companion object {
        @Suppress("RegExpUnnecessaryNonCapturingGroup", "RegExpRedundantEscape")
        private const val DURATION_REGEX = "^(?:[01]\\d|2[0123])\\:(?:[012345]\\d)\\:(?:[012345]\\d)\\.\\d{3}$"
        private const val DATE_REGEX = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12]\\d|3[01])$"

        /**
         * Converts an [Activity] to an [ActivityDTO].
         *
         * @param activity activity to be converted
         * @return [ActivityDTO] representation of the activity
         */
        operator fun invoke(activity: Activity): ActivityDTO =
            ActivityDTO(
                activity.id,
                activity.date.toString(),
                activity.duration.toDTOString(),
                activity.uid,
                activity.sid,
                activity.rid
            )

        /**
         * Checks if a date is valid.
         *
         * @param date date to check
         * @return true if it's valid
         */
        fun isValidDate(date: String): Boolean =
            date.matches(DATE_REGEX.toRegex())

        /**
         * Checks if a duration is valid.
         *
         * @param duration duration to check
         * @return true if it's valid
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
