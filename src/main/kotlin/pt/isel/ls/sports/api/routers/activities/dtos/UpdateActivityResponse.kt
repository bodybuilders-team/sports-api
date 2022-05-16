package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable

/**
 * Represents an activity update response.
 *
 * @property modified true if the activity was modified, false otherwise
 */
@Serializable
data class UpdateActivityResponse(
    val modified: Boolean
)
