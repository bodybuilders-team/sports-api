package pt.isel.ls.sports.services.sections

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.services.AbstractServices

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
            throw AppException.InvalidArgument("Distance must be positive")

        return db.execute { conn ->
            val uid = authenticate(conn, token)

            db.routes.createNewRoute(
                conn,
                startLocation,
                endLocation,
                distance = (distance * 1000).toInt(),
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
    fun getAllRoutes(): List<Route> = db.execute { conn ->
        db.routes.getAllRoutes(conn)
    }
}
