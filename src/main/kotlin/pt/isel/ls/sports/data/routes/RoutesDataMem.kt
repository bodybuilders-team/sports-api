package pt.isel.ls.sports.data.routes

import pt.isel.ls.sports.data.AppDataMemSource
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.errors.AppError

class RoutesDataMem(private val source: AppDataMemSource) : RoutesDatabase {

    override fun createNewRoute(startLocation: String, endLocation: String, distance: Int, uid: Int): Int {
        val id = source.nextRouteId.getAndIncrement()

        if (source.users[uid] == null) throw AppError.notFound("User with id $uid not found")

        source.routes[id] =
            Route(id, start_location = startLocation, end_location = endLocation, distance / 1000.0, uid)

        return id
    }

    override fun getRoute(rid: Int): Route {
        return source.routes[rid] ?: throw AppError.notFound("Route with id $rid not found")
    }

    override fun getAllRoutes(): List<Route> {
        return source.routes.values.toList()
    }

    override fun hasRoute(rid: Int): Boolean =
        source.routes.containsKey(rid)
}
