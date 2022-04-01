package pt.isel.ls.sports.api.routers.activities

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.sports.api.utils.MessageResponse
import pt.isel.ls.sports.api.utils.getErrorResponse
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.pathOrThrow
import pt.isel.ls.sports.api.utils.queryOrThrow
import pt.isel.ls.sports.api.utils.tokenOrThrow
import pt.isel.ls.sports.logRequest
import pt.isel.ls.sports.services.sections.ActivitiesServices
import pt.isel.ls.sports.toIntOrThrow

/**
 * Represents the activity's router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class ActivitiesRouter(private val services: ActivitiesServices) {

    companion object {

        /**
         * Returns the router routes.
         * @param services router services
         * @return router routes
         */
        fun routes(services: ActivitiesServices) = ActivitiesRouter(services).routes
    }

    val routes = routes(
        "/" bind POST to ::createActivity,
        "/search" bind GET to ::searchActivities,
        "/{id}" bind GET to ::getActivity,
        "/{id}" bind DELETE to ::deleteActivity
    )

    /**
     * Creates an activity.
     * @param request activity creation HTTP request
     * @return activity creation HTTP response
     */
    private fun createActivity(request: Request): Response = runCatching {
        logRequest(request)
        val token = request.tokenOrThrow()

        val activityReq = Json.decodeFromString<CreateActivityRequest>(request.bodyString())
        val aid = services.createNewActivity(
            token,
            activityReq.date,
            activityReq.duration,
            activityReq.sid,
            activityReq.rid
        )

        return Response(CREATED).json(CreateActivityResponse(aid))
    }.getOrElse(::getErrorResponse)

    /**
     * Gets a specific activity.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getActivity(request: Request): Response = runCatching {
        logRequest(request)
        val aid = request.pathOrThrow("id").toIntOrThrow { "Invalid Activity Id" }

        val activity = services.getActivity(aid)

        return Response(OK).json(activity)
    }.getOrElse(::getErrorResponse)

    /**
     * Deletes an activity.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun deleteActivity(request: Request): Response = runCatching {
        logRequest(request)
        val token = request.tokenOrThrow()
        val aid = request.pathOrThrow("id").toIntOrThrow { "Invalid Activity Id" }

        services.deleteActivity(token, aid)

        return Response(OK).json(MessageResponse("Activity deleted"))
    }.getOrElse(::getErrorResponse)

    /**
     * Gets all activities, given some parameters in the request query.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun searchActivities(request: Request): Response = runCatching {
        logRequest(request)
        val sid = request.queryOrThrow("sid").toIntOrThrow { "Invalid Sport Id" }
        val orderBy = request.queryOrThrow("orderBy")

        val date = request.query("date")
        val rid = request.query("rid")?.toInt()
        val skip = request.query("skip")?.toInt()
        val limit = request.query("limit")?.toInt()

        val activities = services.getActivities(sid, orderBy, date, rid, limit, skip)

        return Response(OK).json(ActivitiesResponse(activities))
    }.getOrElse(::getErrorResponse)
}
