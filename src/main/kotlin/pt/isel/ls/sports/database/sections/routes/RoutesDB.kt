package pt.isel.ls.sports.database.sections.routes

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.connection.ConnectionDB
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
     * @throws NotFoundException if there's no user with the [uid]
     */
    fun createNewRoute(conn: ConnectionDB, startLocation: String, endLocation: String, distance: Double, uid: Int): Int

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
    fun getAllRoutes(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
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
