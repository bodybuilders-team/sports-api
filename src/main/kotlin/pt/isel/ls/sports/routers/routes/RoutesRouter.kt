package pt.isel.ls.sports.routers.routes

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.sports.SportsServices
import pt.isel.ls.sports.getErrorResponse
import pt.isel.ls.sports.routers.json
import pt.isel.ls.sports.routers.pathOrThrow
import pt.isel.ls.sports.routers.tokenOrThrow
import pt.isel.ls.sports.toIntOrThrow

/**
 * Represents the routes' router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class RoutesRouter(private val services: SportsServices) {

    companion object {

        /**
         * Returns the router routes.
         * @param services router services
         * @return router routes
         */
        fun routes(services: SportsServices) = RoutesRouter(services).routes
    }

    val routes = routes(
        "/" bind POST to ::createRoute,
        "/" bind GET to ::getRoutes,
        "/{id}" bind GET to ::getRoute
    )

    /**
     * Creates a route.
     * @param request route creation HTTP request
     * @return route creation HTTP response
     */
    private fun createRoute(request: Request): Response = runCatching {
        val token = request.tokenOrThrow()

        val routeRequest = Json.decodeFromString<CreateRouteRequest>(request.bodyString())
        val uid = services.createNewRoute(
            token,
            routeRequest.start_location,
            routeRequest.end_location,
            routeRequest.distance
        )

        return Response(CREATED).json(CreateRouteResponse(uid))
    }.getOrElse(::getErrorResponse)

    /**
     * Gets all routes.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getRoutes(request: Request): Response = runCatching {
        val routes = services.getAllRoutes()

        return Response(OK).json(routes)
    }.getOrElse(::getErrorResponse)

    /**
     * Gets a specific route.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getRoute(request: Request): Response = runCatching {
        val rid = request.pathOrThrow("id").toIntOrThrow { "Invalid Route Id" }

        val route = services.getRoute(rid)

        return Response(OK).json(route)
    }.getOrElse(::getErrorResponse)
}
