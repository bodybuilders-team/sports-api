package pt.isel.ls.sports.routers.activities

import kotlinx.serialization.Serializable

/**
 * Represents an activity creation request.
 *
 * @property date activity date
 * @property duration duration of the activity
 * @property sid unique identifier of the activity sport
 * @property rid unique identifier of the activity route (optional)
 */
@Serializable
data class CreateActivityRequest(
    val date: String,
    val duration: String,
    val sid: Int,
    val rid: Int?
)
