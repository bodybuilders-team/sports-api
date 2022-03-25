package pt.isel.ls.sports.routers.sports

import kotlinx.serialization.Serializable

/**
 * Represents a Sport creation request.
 *
 * @property name name of the sport
 * @property description description of the sport (optional)
 * @property uid unique identifier of the user who created the sport
 */
@Serializable
data class CreateSportRequest(
    val name: String,
    val description: String?,
    val uid: Int
)
