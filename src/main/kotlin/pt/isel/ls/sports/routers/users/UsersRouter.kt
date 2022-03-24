package pt.isel.ls.sports.routers.users

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
 * Represents the users' router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class UsersRouter(private val services: SportsServices) {

	companion object {

		/**
		 * Returns the router routes
		 * @param services router services
		 * @return router routes
		 */
		fun routes(services: SportsServices) = UsersRouter(services).routes
	}


	val routes = routes(
		"/users" bind POST to ::createUser,
		"/users" bind GET to ::getUsers,
		"/users/{id}" bind GET to ::getUser,
		"/users/{id}" bind GET to ::getUserActivities,
	)


	/**
	 * Creates a user.
	 * @param request user creation HTTP request
	 * @return user creation HTTP response
	 */
	private fun createUser(request: Request): Response = runCatching {
		val token = request.header("Authorization")
			?: return AppError.noCredentials().toResponse()
		authenticate(token)

		val userRequest = Json.decodeFromString<CreateUserRequest>(request.bodyString())
		val userResponse = services.createNewUser(userRequest.name, userRequest.email)

		return Response(CREATED)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(userResponse))

	}.getOrElse {
		return getErrorResponse(it)
	}


	/**
	 * Gets all users.
	 * @param request HTTP request
	 * @return HTTP response
	 */
	private fun getUsers(request: Request): Response = runCatching {
		val users = services.getAllUsers()

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(users))

	}.getOrElse {
		return getErrorResponse(it)
	}


	/**
	 * Gets a specific user.
	 * @param request HTTP request
	 * @return HTTP response
	 */
	private fun getUser(request: Request): Response = runCatching {
		val uid = request.path("id")?.toInt()
			?: return AppError.badRequest("Invalid User Id").toResponse()

		val user = services.getUser(uid)

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(user))

	}.getOrElse {
		return getErrorResponse(it)
	}


	/**
	 * Gets all activities made by user.
	 * @param request HTTP request
	 * @return HTTP response
	 */
	private fun getUserActivities(request: Request): Response = runCatching {
		val uid = request.path("id")?.toInt()
			?: return AppError.badRequest("Invalid User Id").toResponse()

		val activities = services.getUserActivities(uid)

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(activities))

	}.getOrElse {
		return getErrorResponse(it)
	}
}
