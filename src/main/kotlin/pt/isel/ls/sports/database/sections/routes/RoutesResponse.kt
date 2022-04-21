package pt.isel.ls.sports.database.sections.routes

import pt.isel.ls.sports.domain.Route

/**
 * Represents a response with routes.
 *
 * @property routes list of routes
 */
data class RoutesResponse(val routes: List<Route>, val totalCount: Int)
