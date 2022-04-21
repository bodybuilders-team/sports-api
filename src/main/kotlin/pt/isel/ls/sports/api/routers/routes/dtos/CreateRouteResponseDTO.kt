package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a Route creation response.
 *
 * @property rid of the created route
 */
@Serializable
data class CreateRouteResponseDTO(val rid: Int)
