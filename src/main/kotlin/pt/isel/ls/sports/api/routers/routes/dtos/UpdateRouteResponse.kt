package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a route update response.
 *
 * @property modified true if the route was modified, false otherwise
 */
@Serializable
data class UpdateRouteResponse(
    val modified: Boolean
)
