package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a Sport creation request.
 *
 * @property name name of the sport
 * @property description description of the sport (optional)
 */
@Serializable
data class CreateSportRequest(
    val name: String,
    val description: String? = null
)
