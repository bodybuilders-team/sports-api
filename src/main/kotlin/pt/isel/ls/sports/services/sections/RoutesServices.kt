package pt.isel.ls.sports.services.sections

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.services.AbstractServices
import pt.isel.ls.sports.services.isValidId

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
     *
     * @return the route's unique identifier
     */
    fun createNewRoute(token: String, startLocation: String, endLocation: String, distance: Double): Int {
        val uid = authenticate(token)
        if (!Route.isValidDistance(distance))
            throw AppError.invalidArgument("Distance must be positive")

        return db.routes.createNewRoute(
            startLocation,
            endLocation,
            distance = (distance * 1000).toInt(),
            uid
        )
    }

    /**
     * Get the details of a route.
     *
     * @param rid route's unique identifier
     *
     * @return the route object
     */
    fun getRoute(rid: Int): Route {
        if (!isValidId(rid))
            throw AppError.invalidArgument("Route id must be positive")

        return db.routes.getRoute(rid)
    }

    /**
     * Get the list of routes.
     *
     * @return list of route objects
     */
    fun getAllRoutes(): List<Route> {
        return db.routes.getAllRoutes()
    }
}
