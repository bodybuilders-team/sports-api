package pt.isel.ls.sports.api.routers.routes

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.domain.Route

/**
 * Represents a response with routes.
 *
 * @property routes list of routes
 */
@Serializable
data class RoutesResponse(val routes: List<Route>)
