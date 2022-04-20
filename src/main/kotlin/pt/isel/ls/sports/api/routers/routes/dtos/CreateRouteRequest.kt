package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a Route creation request.
 *
 * @property start_location start location of the route
 * @property end_location end location of the route
 * @property distance distance between [start_location] and [end_location] in meters
 */
@Serializable
data class CreateRouteRequest(
    val start_location: String,
    val end_location: String,
    val distance: Double
)
