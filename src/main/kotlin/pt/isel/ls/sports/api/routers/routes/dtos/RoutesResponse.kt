package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a response with routes.
 *
 * @property routes list of routes
 */
@Serializable
data class RoutesResponse(val routes: List<RouteDTO>)
