package pt.isel.ls.sports.routes

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import pt.isel.ls.sports.AppError
import pt.isel.ls.sports.SportsServices
import pt.isel.ls.sports.authenticate
import pt.isel.ls.sports.errorResponse

@Serializable
data class CreateActivityRequest(
    val date: String,
    val duration: String,
    val sid: Int,
    val rid: Int?
)

@Serializable
data class CreateActivityResponse(
    val aid: Int
)

@Serializable
data class DeleteActivityResponse(
    val message: String
)

class ActivitiesRouter(private val services: SportsServices) {
    companion object {
        fun routes(services: SportsServices) = ActivitiesRouter(services).routes
    }

    val routes = routes(
        "/activities" bind POST to ::createActivity,
        "/activities/{id}" bind GET to ::getActivity,
        "/activities/{id}" bind DELETE to ::deleteActivity,
        "/activities/search" bind DELETE to ::searchActivities
    )

    private fun createActivity(request: Request): Response = runCatching {
        val token = request.header("Authorization") ?: return errorResponse(AppError.NO_CREDENTIALS)
        authenticate(token)

        val activityReq = Json.decodeFromString<CreateActivityRequest>(request.bodyString())
        val aid = services.createActivity(
            token, activityReq.date,
            activityReq.duration, activityReq.sid,
            activityReq.rid
        )

        return Response(CREATED)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(CreateActivityResponse(aid)))
    }.getOrElse {
        return errorResponse(it)
    }

    private fun getActivity(request: Request): Response = runCatching {
        val aid = request.path("id")?.toInt() ?: return errorResponse(AppError.BAD_REQUEST) { "Invalid Activity Id" }

        val activity = services.getActivity(aid)

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(activity))
    }.getOrElse {
        return errorResponse(it)
    }

    private fun deleteActivity(request: Request): Response = runCatching {
        val aid = request.path("id")?.toInt() ?: return errorResponse(AppError.BAD_REQUEST) { "Invalid Activity Id" }

        services.deleteActivity(aid)

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(DeleteActivityResponse("Activity deleted")))

    }.getOrElse {
        return errorResponse(it)
    }

    //TODO Add skip and limit for pagination with search
    private fun searchActivities(request: Request): Response = runCatching {
        val sid = request.path("sid")?.toInt()
            ?: return errorResponse(AppError.BAD_REQUEST) { "Sport Id required" }
        val orderBy = request.path("id")?.toInt()
            ?: return errorResponse(AppError.BAD_REQUEST) { "Order By required" }
        val date = request.path("date")
        val rid = request.path("date")

        val activities = services.searchActivities(sid, orderBy, date, rid)

        return Response(OK)
            .header("Content-Type", "application/json")
            .body(Json.encodeToString(activities))
    }.getOrElse {
        return errorResponse(it)
    }


}


