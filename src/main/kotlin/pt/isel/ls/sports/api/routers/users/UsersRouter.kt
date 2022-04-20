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
import pt.isel.ls.sports.api.routers.IRouterCompanion
import pt.isel.ls.sports.api.routers.activities.dtos.ActivitiesResponse
import pt.isel.ls.sports.api.routers.activities.dtos.ActivityDTO
import pt.isel.ls.sports.api.routers.users.dtos.CreateUserRequest
import pt.isel.ls.sports.api.routers.users.dtos.UserDTO
import pt.isel.ls.sports.api.routers.users.dtos.UsersResponse
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.pathOrThrow
import pt.isel.ls.sports.api.utils.runAndCatch
import pt.isel.ls.sports.services.sections.UsersServices
import pt.isel.ls.sports.utils.toIntOrThrow

/**
 * Represents the users' router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class UsersRouter(private val services: UsersServices) : IRouter {

    companion object : IRouterCompanion<UsersServices> {
        override fun routes(services: UsersServices) = UsersRouter(services).routes
    }

    override val routes = routes(
        "/" bind POST to ::createUser,
        "/" bind GET to ::getUsers,
        "/{id}" bind GET to ::getUser,
        "/{id}/activities" bind GET to ::getUserActivities,
    )

    /**
     * Creates a user.
     * @param request user creation HTTP request
     * @return user creation HTTP response
     */
    private fun createUser(request: Request): Response = runAndCatch {
        val userRequest = request.decodeBodyAs<CreateUserRequest>()
        val userResponse = services.createNewUser(userRequest.name, userRequest.email)

        return Response(CREATED).json(userResponse)
    }

    /**
     * Gets all users.
     * @param request HTTP request
     * @return HTTP response
     */
    @Suppress("UNUSED_PARAMETER")
    private fun getUsers(request: Request): Response = runAndCatch {
        val users = services.getAllUsers()

        return Response(OK).json(UsersResponse(users.map { UserDTO(it) }))
    }

    /**
     * Gets a specific user.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getUser(request: Request): Response = runAndCatch {
        val uid = request.pathOrThrow("id").toIntOrThrow { "Invalid User Id" }

        val user = services.getUser(uid)

        return Response(OK).json(UserDTO(user))
    }

    /**
     * Gets all activities made by user.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getUserActivities(request: Request): Response = runAndCatch {
        val uid = request.pathOrThrow("id").toIntOrThrow { "Invalid User Id" }

        val activities = services.getUserActivities(uid)

        return Response(OK).json(ActivitiesResponse(activities.map { ActivityDTO(it) }))
    }
}
