package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a route update request.
 *
 * @property startLocation new start location of the route (optional)
 * @property endLocation new end location of the route (optional)
 * @property distance new distance of the route (optional)
 */
@Serializable
data class UpdateRouteRequest(
    val startLocation: String? = null,
    val endLocation: String? = null,
    val distance: Double? = null
)
