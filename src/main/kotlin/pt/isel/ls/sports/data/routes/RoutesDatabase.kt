package pt.isel.ls.sports.data.routes

import pt.isel.ls.sports.domain.Route

/**
 * Routes database representation.
 */
interface RoutesDatabase {
    /**
     * Creates a new route.
     *
     * @param startLocation
     * @param endLocation
     * @param distance
     * @param uid user's unique identifier
     *
     * @return the route's unique identifier
     */
    fun createNewRoute(startLocation: String, endLocation: String, distance: Int, uid: Int): Int

    /**
     * Get the details of a route.
     *
     * @param rid route's unique identifier
     */
    fun getRoute(rid: Int): Route

    /**
     * Get the list of routes.
     *
     * @return list of route objects
     */
    fun getAllRoutes(): List<Route>

    /**
     * Verifies if a route exists with the given [rid]
     *
     * @param rid route's unique identifier
     *
     * @return true if the route exists, false otherwise
     */
    fun hasRoute(rid: Int): Boolean
}
