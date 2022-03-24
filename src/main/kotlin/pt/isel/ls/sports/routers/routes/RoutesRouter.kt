package pt.isel.ls.sports.routers.routes

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
		"/routes" bind POST to ::createRoute,
		"/routes" bind GET to ::getRoutes,
		"/routes/{id}" bind GET to ::getRoute
	)


	/**
	 * Creates a route.
	 * @param request route creation HTTP request
	 * @return route creation HTTP response
	 */
	private fun createRoute(request: Request): Response = runCatching {
		val token = request.header("Authorization")
			?: return AppError.noCredentials().toResponse()
		authenticate(token)

		val routeRequest = Json.decodeFromString<CreateRouteRequest>(request.bodyString())
		val uid = services.createNewRoute(
			token,
			routeRequest.start_location,
			routeRequest.end_location,
			routeRequest.distance
		)

		return Response(CREATED)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(CreateRouteResponse(uid)))

	}.getOrElse {
		return getErrorResponse(it)
	}


	/**
	 * Gets all routes.
	 * @param request HTTP request
	 * @return HTTP response
	 */
	private fun getRoutes(request: Request): Response = runCatching {
		val routes = services.getAllRoutes()

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(routes))

	}.getOrElse {
		return getErrorResponse(it)
	}


	/**
	 * Gets a specific route.
	 * @param request HTTP request
	 * @return HTTP response
	 */
	private fun getRoute(request: Request): Response = runCatching {
		//TODO: remove code repetition
		val rid = request.path("id")?.toInt()
			?: return AppError.badRequest("Invalid Route Id").toResponse()

		val route = services.getRoute(rid)

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(route))

	}.getOrElse {
		return getErrorResponse(it)
	}
}
