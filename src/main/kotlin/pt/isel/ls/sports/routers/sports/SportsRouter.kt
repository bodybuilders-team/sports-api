package pt.isel.ls.sports.routers.sports

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import pt.isel.ls.sports.AppError
import pt.isel.ls.sports.SportsServices
import pt.isel.ls.sports.authenticate
import pt.isel.ls.sports.getErrorResponse


/**
 * Represents the sports' router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class SportsRouter(private val services: SportsServices) {

	companion object {

		/**
		 * Returns the router routes
		 * @param services router services
		 * @return router routes
		 */
		fun routes(services: SportsServices) = SportsRouter(services).routes
	}


	val routes = routes(
		"/sports" bind POST to ::createSport,
		"/sports" bind GET to ::getSports,
		"/sports/{id}" bind GET to ::getSport,
		"/sports/{id}/activities" bind GET to ::getSportActivities,
	)


	/**
	 * Creates a sport.
	 * @param request sport creation HTTP request
	 * @return sport creation HTTP response
	 */
	private fun createSport(request: Request): Response = runCatching {
		val token = request.header("Authorization")
			?: return AppError.noCredentials().toResponse()
		authenticate(token)

		val sportRequest = Json.decodeFromString<CreateSportRequest>(request.bodyString())
		val sid = services.createNewSport(
			token, sportRequest.name,
			sportRequest.description
		)

		return Response(Status.CREATED)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(CreateSportResponse(sid)))

	}.getOrElse {
		return getErrorResponse(it)
	}


	/**
	 * Gets all sports.
	 * @param request HTTP request
	 * @return HTTP response
	 */
	private fun getSports(request: Request): Response = runCatching {
		val sports = services.getAllSports()

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(sports))

	}.getOrElse {
		return getErrorResponse(it)
	}


	/**
	 * Gets a specific sport.
	 * @param request HTTP request
	 * @return HTTP response
	 */
	private fun getSport(request: Request): Response = runCatching {
		val sid = request.path("id")?.toInt()
			?: return AppError.badRequest("Invalid Sport Id").toResponse()

		val sport = services.getSport(sid)

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(sport))

	}.getOrElse {
		return getErrorResponse(it)
	}

	private fun getSportActivities(request: Request): Response = runCatching {
		val sid = request.path("id")?.toInt()
			?: return AppError.badRequest("Invalid Sport Id").toResponse()

		val activities = services.getSportActivities(sid)

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(activities))

	}.getOrElse {
		return getErrorResponse(it)
	}
}
