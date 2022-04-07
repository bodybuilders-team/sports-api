package pt.isel.ls.sports.database.sections.routes

import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.errors.AppError

class RoutesMemoryDB(private val source: AppMemoryDBSource) : RoutesDB {

    override fun createNewRoute(
        conn: ConnectionDB,
        startLocation: String,
        endLocation: String,
        distance: Int,
        uid: Int
    ): Int {
        val id = source.nextRouteId.getAndIncrement()

        if (source.users[uid] == null) throw AppError.NotFound("User with id $uid not found")

        source.routes[id] = Route(id, startLocation, endLocation, distance / 1000.0, uid)

        return id
    }

    override fun getRoute(
        conn: ConnectionDB,
        rid: Int
    ): Route {
        return source.routes[rid] ?: throw AppError.NotFound("Route with id $rid not found")
    }

    override fun getAllRoutes(
        conn: ConnectionDB,
    ): List<Route> {
        return source.routes.values.toList()
    }

    override fun hasRoute(
        conn: ConnectionDB,
        rid: Int
    ): Boolean =
        source.routes.containsKey(rid)
}
