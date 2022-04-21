package pt.isel.ls.sports.database.sections.routes

import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.errors.AppException

class RoutesMemoryDB(private val source: AppMemoryDBSource) : RoutesDB {

    override fun createNewRoute(
        conn: ConnectionDB,
        startLocation: String,
        endLocation: String,
        distance: Int,
        uid: Int
    ): Int {
        val id = source.nextRouteId.getAndIncrement()

        if (source.users[uid] == null) throw AppException.NotFound("User with id $uid not found")

        source.routes[id] = Route(id, startLocation, endLocation, distance / 1000.0, uid)

        return id
    }

    override fun getRoute(
        conn: ConnectionDB,
        rid: Int
    ): Route {
        return source.routes[rid] ?: throw AppException.NotFound("Route with id $rid not found")
    }

    override fun getAllRoutes(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): RoutesResponse = RoutesResponse(
        source.routes.values.toList(), 0
    )

    override fun hasRoute(
        conn: ConnectionDB,
        rid: Int
    ): Boolean =
        source.routes.containsKey(rid)
}
