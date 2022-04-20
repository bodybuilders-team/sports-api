package pt.isel.ls.sports.api.routers.routes

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.sports.api.routers.IRouter
import pt.isel.ls.sports.api.routers.IRouterCompanion
import pt.isel.ls.sports.api.routers.routes.dtos.CreateRouteRequest
import pt.isel.ls.sports.api.routers.routes.dtos.CreateRouteResponse
import pt.isel.ls.sports.api.routers.routes.dtos.RouteDTO
import pt.isel.ls.sports.api.routers.routes.dtos.RoutesResponse
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.pathOrThrow
import pt.isel.ls.sports.api.utils.runAndCatch
import pt.isel.ls.sports.api.utils.tokenOrThrow
import pt.isel.ls.sports.services.sections.RoutesServices
import pt.isel.ls.sports.utils.toIntOrThrow

/**
 * Represents the routes' router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class RoutesRouter(private val services: RoutesServices) : IRouter {

    companion object : IRouterCompanion<RoutesServices> {
        override fun routes(services: RoutesServices) = RoutesRouter(services).routes
    }

    override val routes = routes(
        "/" bind POST to ::createRoute,
        "/" bind GET to ::getRoutes,
        "/{id}" bind GET to ::getRoute
    )

    /**
     * Creates a route.
     * @param request route creation HTTP request
     * @return route creation HTTP response
     */
    private fun createRoute(request: Request): Response = runAndCatch {
        val token = request.tokenOrThrow()

        val routeRequest = request.decodeBodyAs<CreateRouteRequest>()
        val uid = services.createNewRoute(
            token,
            routeRequest.start_location,
            routeRequest.end_location,
            routeRequest.distance
        )

        return Response(CREATED).json(CreateRouteResponse(uid))
    }

    /**
     * Gets all routes.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getRoutes(request: Request): Response = runAndCatch {
        val routes = services.getAllRoutes()

        return Response(OK).json(RoutesResponse(routes.map { RouteDTO(it) }))
    }

    /**
     * Gets a specific route.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getRoute(request: Request): Response = runAndCatch {
        val rid = request.pathOrThrow("id").toIntOrThrow { "Invalid Route Id" }

        val route = services.getRoute(rid)

        return Response(OK).json(RouteDTO(route))
    }
}
