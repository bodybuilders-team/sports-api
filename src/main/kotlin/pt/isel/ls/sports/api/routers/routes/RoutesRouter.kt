package pt.isel.ls.sports.api.routers.routes

import org.http4k.core.Method.GET
import org.http4k.core.Method.PATCH
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.sports.api.routers.IRouter
import pt.isel.ls.sports.api.routers.routes.dtos.CreateRouteRequest
import pt.isel.ls.sports.api.routers.routes.dtos.CreateRouteResponse
import pt.isel.ls.sports.api.routers.routes.dtos.RouteDTO
import pt.isel.ls.sports.api.routers.routes.dtos.RoutesResponseDTO
import pt.isel.ls.sports.api.routers.routes.dtos.UpdateRouteRequest
import pt.isel.ls.sports.api.routers.routes.dtos.UpdateRouteResponse
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.errors.runAndCatch
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.pathOrThrow
import pt.isel.ls.sports.api.utils.toIntOrThrow
import pt.isel.ls.sports.api.utils.tokenOrThrow
import pt.isel.ls.sports.services.sections.routes.RoutesServices

/**
 * Represents the routes' router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class RoutesRouter(private val services: RoutesServices) : IRouter {

    companion object {
        private const val DEFAULT_SKIP = 0
        private const val DEFAULT_LIMIT = 10

        /**
         * Returns the routes' router's routes.
         *
         * @param services routes services
         * @return routes router's routes
         */
        fun routes(services: RoutesServices) = RoutesRouter(services).routes
    }

    override val routes = routes(
        "/" bind POST to ::createRoute,
        "/" bind GET to ::searchRoutes,
        "/{id}" bind PATCH to ::updateRoute,
        "/{id}" bind GET to ::getRoute
    )

    /**
     * Creates a route.
     *
     * @param request HTTP request containing a body that follows the [CreateRouteRequest] format
     * @return HTTP response containing a body that follows the [CreateRouteResponse] format
     */
    private fun createRoute(request: Request): Response = runAndCatch {
        val token = request.tokenOrThrow()

        val routeRequest = request.decodeBodyAs<CreateRouteRequest>()
        val uid = services.createNewRoute(
            token,
            routeRequest.startLocation,
            routeRequest.endLocation,
            routeRequest.distance
        )

        return Response(CREATED).json(CreateRouteResponse(uid))
    }

    /**
     * Updates a route.
     *
     * @param request HTTP request containing a body that follows the [UpdateRouteRequest] format
     * @return HTTP response containing a body that follows the [UpdateRouteResponse] format
     */
    private fun updateRoute(request: Request): Response = runAndCatch {
        val token = request.tokenOrThrow()
        val id = request.pathOrThrow("id").toIntOrThrow { "Invalid Route Id" }

        val routeRequest = request.decodeBodyAs<UpdateRouteRequest>()
        val modified = services.updateRoute(
            id,
            token,
            routeRequest.startLocation,
            routeRequest.endLocation,
            routeRequest.distance
        )

        return Response(OK).json(UpdateRouteResponse(modified))
    }

    /**
     * Searches routes.
     *
     * @param request HTTP request
     * @return HTTP response containing a body that follows the [RoutesResponseDTO] format
     */
    private fun searchRoutes(request: Request): Response = runAndCatch {
        val startLocation = request.query("startLocation")
        val endLocation = request.query("endLocation")
        val skip = request.query("skip")?.toIntOrThrow { "Invalid skip" } ?: DEFAULT_SKIP
        val limit = request.query("limit")?.toIntOrThrow { "Invalid limit" } ?: DEFAULT_LIMIT

        val routesResponse = services.searchRoutes(skip, limit, startLocation, endLocation)

        return Response(OK).json(RoutesResponseDTO(routesResponse))
    }

    /**
     * Gets a specific route.
     *
     * @param request HTTP request
     * @return HTTP response containing a body that follows the [RouteDTO] format
     */
    private fun getRoute(request: Request): Response = runAndCatch {
        val id = request.pathOrThrow("id").toIntOrThrow { "Invalid Route Id" }

        val route = services.getRoute(id)

        return Response(OK).json(RouteDTO(route))
    }
}
