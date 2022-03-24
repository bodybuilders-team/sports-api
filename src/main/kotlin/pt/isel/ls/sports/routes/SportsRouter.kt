package pt.isel.ls.sports.routes

import kotlinx.serialization.Serializable
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
import pt.isel.ls.sports.errorResponse

@Serializable
data class CreateSportRequest(val name: String, val description: String?, val uid: Int)

@Serializable
data class CreateSportResponse(val sid: Int)

class SportsRouter(private val services: SportsServices) {
    companion object {
        fun routes(services: SportsServices) = SportsRouter(services).routes
    }

    val routes = routes(
        "/sports" bind POST to ::createSport,
        "/sports" bind GET to ::getSports,
        "/sports/{id}" bind GET to ::getSport,
        "/sports/{id}/activities" bind GET to ::getSportActivities,
    )

    private fun createSport(request: Request): Response = runCatching {
        val token = request.header("Authorization") ?: return errorResponse(AppError.NO_CREDENTIALS)
        authenticate(token)

        val sportRequest = Json.decodeFromString<CreateSportRequest>(request.bodyString())
        val sid = services.createSport(
            token, sportRequest.name,
            sportRequest.description, sportRequest.uid
        )

        return Response(Status.CREATED)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(CreateSportResponse(sid)))

    }.getOrElse {
        return errorResponse(it)
    }

    private fun getSports(request: Request): Response = runCatching {
        val sports = services.getSports()

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(sports))
    }.getOrElse {
        return errorResponse(it)
    }

    private fun getSport(request: Request): Response = runCatching {
        val sid = request.path("id")?.toInt() ?: return errorResponse(AppError.BAD_REQUEST) { "Invalid Sport Id" }

        val sport = services.getSport(sid)

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(sport))
    }.getOrElse {
        return errorResponse(it)
    }

    private fun getSportActivities(request: Request): Response = runCatching {
        val sid = request.path("id")?.toInt() ?: return errorResponse(AppError.BAD_REQUEST) { "Invalid Sport Id" }

        val activities = services.getActivitiesOfSport(sid)

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(activities))
    }.getOrElse {
        return errorResponse(it)
    }

}


