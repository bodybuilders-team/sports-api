package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a sport update request.
 *
 * @property name new name of the sport (optional)
 * @property description new description of the sport (optional)
 */
@Serializable
data class UpdateSportRequest(
    val name: String? = null,
    val description: String? = null
)
