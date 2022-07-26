package pt.isel.ls.sports.services.sections.routes

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.database.sections.routes.RoutesResponse
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.services.AbstractServices
import pt.isel.ls.sports.services.exceptions.AuthenticationException
import pt.isel.ls.sports.services.exceptions.AuthorizationException

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
     * Updates a route.
     *
     * @param id route's unique identifier
     * @param token user's token
     * @param startLocation new start location of the route
     * @param endLocation new end location of the route
     * @param distance new distance to travel
     *
     * @return true if the route was successfully modified, false otherwise
     * @throws InvalidArgumentException if [id] is negative
     * @throws InvalidArgumentException if [distance] is invalid
     * @throws AuthenticationException if a user with the [token] was not found
     * @throws NotFoundException if there's no route with the [id]
     * @throws AuthorizationException if the user with the [token] is not the owner of the route
     * @throws InvalidArgumentException if [startLocation], [endLocation] and [distance] are both null
     */
    fun updateRoute(id: Int, token: String, startLocation: String?, endLocation: String?, distance: Double?): Boolean {
        validateRid(id)
        if (distance != null && !Route.isValidDistance(distance))
            throw InvalidArgumentException("Invalid distance: $distance")

        return db.execute { conn ->
            val uid = authenticate(conn, token)

            val route = db.routes.getRoute(conn, id)
            if (route.uid != uid)
                throw AuthorizationException("You are not allowed to update this route.")

            db.routes.updateRoute(conn, id, startLocation, endLocation, distance)
        }
    }

    /**
     * Gets a specific route.
     *
     * @param id route's unique identifier
     *
     * @return the route object
     * @throws InvalidArgumentException if the [id] is negative
     * @throws NotFoundException if there's no route with the [id]
     */
    fun getRoute(id: Int): Route {
        validateRid(id)

        return db.execute { conn ->
            db.routes.getRoute(conn, id)
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
    fun searchRoutes(
        skip: Int,
        limit: Int,
        startLocation: String? = null,
        endLocation: String? = null
    ): RoutesResponse {
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        return db.execute { conn ->
            db.routes.searchRoutes(conn, skip, limit, startLocation, endLocation)
        }
    }

    companion object {
        private val LIMIT_RANGE = 0..100
    }
}
