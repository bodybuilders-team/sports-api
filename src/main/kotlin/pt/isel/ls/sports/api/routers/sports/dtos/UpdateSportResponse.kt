package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a sport update response.
 *
 * @property modified true if the sport was modified, false otherwise
 */
@Serializable
data class UpdateSportResponse(
    val modified: Boolean
)
