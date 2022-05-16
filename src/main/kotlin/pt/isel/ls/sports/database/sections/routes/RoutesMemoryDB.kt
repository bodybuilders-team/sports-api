package pt.isel.ls.sports.database.sections.routes

import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.domain.Route

/**
 * Routes database representation using memory.
 */
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

    override fun updateRoute(
        conn: ConnectionDB,
        rid: Int,
        startLocation: String?,
        endLocation: String?,
        distance: Double?
    ): Boolean {
        val prevRoute = source.routes[rid] ?: throw NotFoundException("Route not found.")

        if (startLocation == null && endLocation == null && distance == null)
            throw InvalidArgumentException("Start location, end location or distance must be specified.")

        val newRoute = prevRoute.copy(
            startLocation = startLocation ?: prevRoute.startLocation,
            endLocation = endLocation ?: prevRoute.endLocation,
            distance = distance ?: prevRoute.distance
        )
        source.routes[rid] = newRoute

        return prevRoute != newRoute
    }

    override fun getRoute(
        conn: ConnectionDB,
        rid: Int
    ): Route =
        source.routes[rid] ?: throw NotFoundException("Route with id $rid not found")

    override fun searchRoutes(
        conn: ConnectionDB,
        skip: Int,
        limit: Int,
        startLocation: String?,
        endLocation: String?
    ): RoutesResponse = RoutesResponse(
        routes = source.routes
            .values.toList()
            .run { subList(skip, if (lastIndex + 1 < limit) lastIndex + 1 else limit) },
        totalCount = source.routes.size
    )

    override fun hasRoute(
        conn: ConnectionDB,
        rid: Int
    ): Boolean =
        source.routes.containsKey(rid)
}
