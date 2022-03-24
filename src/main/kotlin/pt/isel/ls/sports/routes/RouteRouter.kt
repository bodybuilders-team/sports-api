package pt.isel.ls.sports.routes

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import pt.isel.ls.sports.*


@Serializable
data class CreateRouteRequest(val start_location: String, val end_location: String, val distance: Double)

@Serializable
data class CreateRouteResponse(val rid: Int)

class RouteRouter(private val services: SportsServices) {
    companion object {
        fun routes(services: SportsServices) = RouteRouter(services).routes
    }

    val routes = routes(
        "/routes" bind POST to ::createRoute,
        "/routes" bind GET to ::getRoutes,
        "/routes/{id}" bind GET to ::getRoute
    )

    private fun createRoute(request: Request): Response = runCatching {
        val token = request.header("Authorization") ?: return errorResponse(AppError.NO_CREDENTIALS)
        authenticate(token)

        val routeRequest = Json.decodeFromString<CreateRouteRequest>(request.bodyString())

        val uid = services.createRoute(
            token, routeRequest.start_location,
            routeRequest.end_location, routeRequest.distance
        )

        return Response(CREATED)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(CreateRouteResponse(uid)))
    }.getOrElse {
        return errorResponse(it)
    }

    private fun getRoutes(request: Request): Response = runCatching {
        val routes = services.getRoutes()

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(routes))
    }.getOrElse {
        return errorResponse(it)
    }

    private fun getRoute(request: Request): Response = runCatching {
        //TODO: remove code repetition
        val rid = request.path("id")?.toInt() ?: return errorResponse(AppError.BAD_REQUEST) { "Invalid Route Id" }

        val route = services.getRoute(rid)

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(route))

    }.getOrElse {
        return errorResponse(it)
    }


}


