package pt.isel.ls.sports.database.sections.routes

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.domain.Route

/**
 * Routes database representation.
 */
interface RoutesDB {

    /**
     * Creates a new route.
     *
     * @param conn database Connection
     * @param startLocation start location of the route
     * @param endLocation end location of the route
     * @param distance distance to travel between [startLocation] and [endLocation] in kilometers
     * @param uid user's unique identifier
     *
     * @return route's unique identifier
     */
    fun createNewRoute(conn: ConnectionDB, startLocation: String, endLocation: String, distance: Double, uid: Int): Int

    fun updateRoute(conn: ConnectionDB, rid: Int, startLocation: String?, endLocation: String?): Boolean

    /**
     * Gets a specific route.
     *
     * @param conn database Connection
     * @param rid route's unique identifier
     *
     * @return the route object
     * @throws NotFoundException if there's no route with the [rid]
     */
    fun getRoute(conn: ConnectionDB, rid: Int): Route

    /**
     * Gets all routes.
     *
     * @param conn database Connection
     *
     * @return [RoutesResponse] with the list of routes
     */
    fun searchRoutes(
        conn: ConnectionDB,
        skip: Int,
        limit: Int,
        startLocation: String? = null,
        endLocation: String? = null
    ): RoutesResponse

    /**
     * Verifies if a route with the given [rid] exists.
     *
     * @param conn database Connection
     * @param rid route's unique identifier
     *
     * @return true if the route exists, false otherwise
     */
    fun hasRoute(conn: ConnectionDB, rid: Int): Boolean
}
