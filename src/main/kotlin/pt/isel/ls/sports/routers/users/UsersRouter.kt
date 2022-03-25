package pt.isel.ls.sports.routers.users

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
import pt.isel.ls.sports.toIntOrThrow

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
        "/" bind POST to ::createUser,
        "/" bind GET to ::getUsers,
        "/{id}" bind GET to ::getUser,
        "/{id}" bind GET to ::getUserActivities,
    )

    /**
     * Creates a user.
     * @param request user creation HTTP request
     * @return user creation HTTP response
     */
    private fun createUser(request: Request): Response = runCatching {
        val userRequest = Json.decodeFromString<CreateUserRequest>(request.bodyString())
        val userResponse = services.createNewUser(userRequest.name, userRequest.email)

        return Response(CREATED).json(userResponse)
    }.getOrElse(::getErrorResponse)

    /**
     * Gets all users.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getUsers(request: Request): Response = runCatching {
        val users = services.getAllUsers()

        return Response(OK).json(users)
    }.getOrElse(::getErrorResponse)

    /**
     * Gets a specific user.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getUser(request: Request): Response = runCatching {
        val uid = request.pathOrThrow("id").toIntOrThrow { "Invalid User Id" }

        val user = services.getUser(uid)

        return Response(OK).json(user)
    }.getOrElse(::getErrorResponse)

    /**
     * Gets all activities made by user.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getUserActivities(request: Request): Response = runCatching {
        val uid = request.pathOrThrow("id").toIntOrThrow { "Invalid User Id" }

        val activities = services.getUserActivities(uid)

        return Response(OK).json(activities)
    }.getOrElse(::getErrorResponse)
}
