package pt.isel.ls.sports.services.sections.routes

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.InvalidArgumentException
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.sections.routes.RoutesResponse
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.services.AbstractServices
import pt.isel.ls.sports.services.AuthenticationException

/**
 * Routes services. Implements methods regarding routes.
 */
class RoutesServices(db: AppDB) : AbstractServices(db) {
    /**
     * Creates a new route.
     *
     * @param token user's token
     * @param startLocation start location of the route
     * @param endLocation end location of the route
     * @param distance distance to travel between [startLocation] and [endLocation] in kilometers
     *
     * @return route's unique identifier
     * @throws InvalidArgumentException if the [distance] is negative
     * @throws AuthenticationException if a user with the [token] was not found
     */
    fun createNewRoute(token: String, startLocation: String, endLocation: String, distance: Double): Int {
        if (!Route.isValidDistance(distance))
            throw InvalidArgumentException("Distance must be positive")

        return db.execute { conn ->
            val uid = authenticate(conn, token)

            db.routes.createNewRoute(
                conn,
                startLocation,
                endLocation,
                distance,
                uid
            )
        }
    }

    /**
     * Gets a specific route.
     *
     * @param rid route's unique identifier
     *
     * @return the route object
     * @throws InvalidArgumentException if the [rid] is negative
     * @throws NotFoundException if there's no route with the [rid]
     */
    fun getRoute(rid: Int): Route {
        validateRid(rid)

        return db.execute { conn ->
            db.routes.getRoute(conn, rid)
        }
    }

    /**
     * Gets all routes.
     *
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [RoutesResponse] with the list of routes
     * @throws InvalidArgumentException if [skip] is invalid
     * @throws InvalidArgumentException if [limit] is invalid
     */
    fun getAllRoutes(skip: Int, limit: Int): RoutesResponse {
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        return db.execute { conn ->
            db.routes.getAllRoutes(conn, skip, limit)
        }
    }

    companion object {
        private val LIMIT_RANGE = 0..100
    }
}
