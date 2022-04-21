package pt.isel.ls.sports.database.sections.routes

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
     * @param startLocation
     * @param endLocation
     * @param distance
     * @param uid user's unique identifier
     *
     * @return the route's unique identifier
     */
    fun createNewRoute(conn: ConnectionDB, startLocation: String, endLocation: String, distance: Int, uid: Int): Int

    /**
     * Get the details of a route.
     *
     * @param conn database Connection
     * @param rid route's unique identifier
     */
    fun getRoute(conn: ConnectionDB, rid: Int): Route

    /**
     * Get the list of routes.
     *
     * @param conn database Connection
     *
     * @return list of route objects
     */
    fun getAllRoutes(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): RoutesResponse

    /**
     * Verifies if a route exists with the given [rid]
     *
     * @param conn database Connection
     * @param rid route's unique identifier
     *
     * @return true if the route exists, false otherwise
     */
    fun hasRoute(conn: ConnectionDB, rid: Int): Boolean
}
