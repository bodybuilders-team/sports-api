package pt.isel.ls.sports.api.routers.users

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.sports.api.routers.IRouter
import pt.isel.ls.sports.api.routers.activities.dtos.ActivitiesResponseDTO
import pt.isel.ls.sports.api.routers.users.dtos.CreateUserRequest
import pt.isel.ls.sports.api.routers.users.dtos.CreateUserResponse
import pt.isel.ls.sports.api.routers.users.dtos.LoginUserRequest
import pt.isel.ls.sports.api.routers.users.dtos.LoginUserResponseDTO
import pt.isel.ls.sports.api.routers.users.dtos.UserDTO
import pt.isel.ls.sports.api.routers.users.dtos.UsersResponseDTO
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.errors.runAndCatch
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.pathOrThrow
import pt.isel.ls.sports.api.utils.toIntOrThrow
import pt.isel.ls.sports.services.sections.users.UsersServices

/**
 * Represents the users' router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class UsersRouter(private val services: UsersServices) : IRouter {

    companion object {
        const val DEFAULT_SKIP = 0
        const val DEFAULT_LIMIT = 10

        /**
         * Returns the users' router's routes.
         *
         * @param services users services
         * @return users router's routes
         */
        fun routes(services: UsersServices) = UsersRouter(services).routes
    }

    override val routes = routes(
        "/" bind POST to ::createUser,
        "/" bind GET to ::getUsers,
        "/login" bind POST to ::loginUser,
        "/{id}" bind GET to ::getUser,
        "/{id}/activities" bind GET to ::getUserActivities
    )

    /**
     * Creates a user.
     *
     * @param request HTTP request containing a body that follows the [CreateUserRequest] format
     * @return HTTP response containing a body that follows the [CreateUserResponse] format
     */
    private fun createUser(request: Request): Response = runAndCatch {
        val userRequest = request.decodeBodyAs<CreateUserRequest>()
        val userResponse = services.createNewUser(userRequest.name, userRequest.email, userRequest.password)

        return Response(CREATED).json(CreateUserResponse(userResponse))
    }

    /**
     * Logs a user in, by providing a token in exchange for a valid email and password.
     *
     * @param request HTTP request containing a body that follows the [LoginUserRequest] format
     * @return HTTP response containing a body that follows the [LoginUserResponseDTO] format
     */
    private fun loginUser(request: Request): Response = runAndCatch {
        val userRequest = request.decodeBodyAs<LoginUserRequest>()
        val userResponse = services.loginUser(userRequest.email, userRequest.password)

        return Response(OK).json(LoginUserResponseDTO(userResponse))
    }

    /**
     * Gets all users.
     *
     * @param request HTTP request
     * @return HTTP response containing a body that follows the [UsersResponseDTO] format
     */
    private fun getUsers(request: Request): Response = runAndCatch {
        val skip = request.query("skip")?.toIntOrThrow { "Invalid skip" } ?: DEFAULT_SKIP
        val limit = request.query("limit")?.toIntOrThrow { "Invalid limit" } ?: DEFAULT_LIMIT
        val usersResponse = services.getAllUsers(skip, limit)

        return Response(OK).json(UsersResponseDTO(usersResponse))
    }

    /**
     * Gets a specific user.
     *
     * @param request HTTP request
     * @return HTTP response containing a body that follows the [UserDTO] format
     */
    private fun getUser(request: Request): Response = runAndCatch {
        val id = request.pathOrThrow("id").toIntOrThrow { "Invalid User Id" }

        val user = services.getUser(id)

        return Response(OK).json(UserDTO(user))
    }

    /**
     * Gets all the activities made by a specific user.
     *
     * @param request HTTP request
     * @return HTTP response containing a body that follows the [ActivitiesResponseDTO] format
     */
    private fun getUserActivities(request: Request): Response = runAndCatch {
        val id = request.pathOrThrow("id").toIntOrThrow { "Invalid User Id" }
        val skip = request.query("skip")?.toIntOrThrow { "Invalid skip" } ?: DEFAULT_SKIP
        val limit = request.query("limit")?.toIntOrThrow { "Invalid limit" } ?: DEFAULT_LIMIT

        val activitiesResponse = services.getUserActivities(id, skip, limit)

        return Response(OK).json(ActivitiesResponseDTO(activitiesResponse))
    }
}
