package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a route creation request.
 *
 * @property startLocation start location of the route
 * @property endLocation end location of the route
 * @property distance distance to travel between [startLocation] and [endLocation] in kilometers
 */
@Serializable
data class CreateRouteRequest(
    val startLocation: String,
    val endLocation: String,
    val distance: Double
)
