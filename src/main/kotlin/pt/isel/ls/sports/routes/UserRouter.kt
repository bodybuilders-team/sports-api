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
data class CreateUserRequest(val name: String, val email: String)

@Serializable
data class CreateUserResponse(val uid: Int)

class UserRouter(private val services: SportsServices) {
    companion object {
        fun routes(services: SportsServices) = UserRouter(services).routes
    }

    val routes = routes(
        "/users" bind POST to ::createUser,
        "/users" bind GET to ::getUsers,
        "/users/{id}" bind GET to ::getUser,
        "/users/{id}" bind GET to ::getActivitiesMadeByUser,
    )

    private fun createUser(request: Request): Response = runCatching {
        val token = request.header("Authorization") ?: return errorResponse(AppError.NO_CREDENTIALS)
        authenticate(token)

        val userRequest = Json.decodeFromString<CreateUserRequest>(request.bodyString())
        val uid = services.createUser(token, userRequest.name, userRequest.email)

        return Response(CREATED)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(CreateUserResponse(uid)))

    }.getOrElse {
        return errorResponse(it)
    }

    private fun getUsers(request: Request): Response = runCatching {
        val users = services.getUsers()

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(users))

    }.getOrElse {
        return errorResponse(it)
    }

    private fun getUser(request: Request): Response = runCatching {
        val uid = request.path("id")?.toInt() ?: return errorResponse(AppError.BAD_REQUEST) { "Invalid User Id" }

        val user = services.getUser(uid)

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(user))
    }.getOrElse {
        return errorResponse(it)
    }

    private fun getActivitiesMadeByUser(request: Request): Response = runCatching {
        val uid = request.path("id")?.toInt() ?: return errorResponse(AppError.BAD_REQUEST) { "Invalid User Id" }

        val activities = services.getActivitiesMadeByUser(uid)

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(activities))

    }.getOrElse {
        return errorResponse(it)
    }
}


