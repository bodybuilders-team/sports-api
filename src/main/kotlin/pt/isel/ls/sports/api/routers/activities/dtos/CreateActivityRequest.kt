package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException

/**
 * Represents an activity creation request.
 *
 * @property date date of the activity in string representation
 * @property duration duration of the activity in string representation
 * @property sid unique identifier of the activity sport
 * @property rid unique identifier of the activity route (optional)
 */
@Serializable
data class CreateActivityRequest(
    val date: String,
    val duration: String,
    val sid: Int,
    val rid: Int? = null
) {
    init {
        if (!ActivityDTO.isValidDate(date))
            throw InvalidArgumentException("Date must be in the format yyyy-mm-dd")

        if (!ActivityDTO.isValidDuration(duration))
            throw InvalidArgumentException("Duration must be in the format hh:mm:ss.fff")
    }
}
