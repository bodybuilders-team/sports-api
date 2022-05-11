package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a sport update request.
 *
 * @property name name of the sport (optional)
 * @property description description of the sport (optional)
 */
@Serializable
data class UpdateSportRequest(
    val name: String? = null,
    val description: String? = null
)
