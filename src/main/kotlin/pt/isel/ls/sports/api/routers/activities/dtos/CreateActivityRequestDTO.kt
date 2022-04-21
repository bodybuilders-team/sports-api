package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.errors.AppException

/**
 * Represents an activity creation request.
 *
 * @property date activity date
 * @property duration duration of the activity
 * @property sid unique identifier of the activity sport
 * @property rid unique identifier of the activity route (optional)
 */
@Serializable
data class CreateActivityRequestDTO(
    val date: String,
    val duration: String,
    val sid: Int,
    val rid: Int? = null
) {
    init {
        if (!ActivityDTO.isValidDate(date))
            throw AppException.InvalidArgument("Date must be in the format yyyy-mm-dd")

        if (!ActivityDTO.isValidDuration(duration))
            throw AppException.InvalidArgument("Duration must be in the format hh:mm:ss.fff")
    }
}
