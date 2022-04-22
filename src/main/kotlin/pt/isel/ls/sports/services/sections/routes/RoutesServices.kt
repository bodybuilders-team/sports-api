package pt.isel.ls.sports.services.sections.routes

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.sections.routes.RoutesResponse
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.services.AbstractServices
import pt.isel.ls.sports.services.InvalidArgumentException

class RoutesServices(db: AppDB) : AbstractServices(db) {
    /**
     * Creates a new route.
     *
     * The [distance] is converted from km to m (meters).
     *
     * @param token user's token
     * @param startLocation
     * @param endLocation
     * @param distance distance of the route in km
     *
     * @return the route's unique identifier
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
     * Get the details of a route.
     *
     * @param rid route's unique identifier
     *
     * @return the route object
     */
    fun getRoute(rid: Int): Route {
        validateRid(rid)

        return db.execute { conn ->
            db.routes.getRoute(conn, rid)
        }
    }

    /**
     * Get the list of routes.
     *
     * @return list of route objects
     */
    fun getAllRoutes(skip: Int, limit: Int): RoutesResponse = db.execute { conn ->
        db.routes.getAllRoutes(conn, skip, limit)
    }
}
