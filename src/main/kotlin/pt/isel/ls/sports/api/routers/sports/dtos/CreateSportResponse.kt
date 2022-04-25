package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a sport creation response.
 *
 * @property sid sport's unique identifier
 */
@Serializable
data class CreateSportResponse(val sid: Int)
