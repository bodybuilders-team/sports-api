package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable

/**
 * Represents an activity update request.
 *
 * @property date new date of the activity in string representation (optional)
 * @property duration new duration of the activity in string representation (optional)
 * @property sid new sport id of the activity (optional)
 * @property rid new route id of the activity (optional)
 */
@Serializable
data class UpdateActivityRequest(
    val date: String? = null,
    val duration: String? = null,
    val sid: Int? = null,
    val rid: Int? = null
)
