package pt.isel.ls.sports.database.sections.routes

import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.domain.Route

class RoutesMemoryDB(private val source: AppMemoryDBSource) : RoutesDB {

    override fun createNewRoute(
        conn: ConnectionDB,
        startLocation: String,
        endLocation: String,
        distance: Double,
        uid: Int
    ): Int {
        val id = source.nextRouteId.getAndIncrement()

        source.users[uid] ?: throw NotFoundException("User with id $uid not found")

        source.routes[id] = Route(id, startLocation, endLocation, distance, uid)

        return id
    }

    override fun getRoute(
        conn: ConnectionDB,
        rid: Int
    ): Route =
        source.routes[rid] ?: throw NotFoundException("Route with id $rid not found")

    override fun getAllRoutes(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): RoutesResponse = RoutesResponse(
        routes = source.routes
            .values.toList()
            .run { subList(skip, if (lastIndex + 1 < limit) lastIndex + 1 else limit) },
        totalCount = 0
    )

    override fun hasRoute(
        conn: ConnectionDB,
        rid: Int
    ): Boolean =
        source.routes.containsKey(rid)
}
