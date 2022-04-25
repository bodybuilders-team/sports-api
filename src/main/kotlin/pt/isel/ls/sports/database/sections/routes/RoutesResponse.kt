package pt.isel.ls.sports.database.sections.routes

import pt.isel.ls.sports.domain.Route

/**
 * Represents a response with routes.
 *
 * The number of routes depends on pagination.
 * [totalCount] represents the total number of routes that could have been retrieved regardless of pagination,
 * and as such is used for calculation of page numbers.
 *
 * @property routes list of routes
 * @property totalCount total number of routes
 */
data class RoutesResponse(val routes: List<Route>, val totalCount: Int)
