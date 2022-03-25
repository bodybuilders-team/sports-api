package pt.isel.ls.sports.routers.activities

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
import pt.isel.ls.sports.SportsServices
import pt.isel.ls.sports.getErrorResponse
import pt.isel.ls.sports.routers.json
import pt.isel.ls.sports.routers.pathOrThrow
import pt.isel.ls.sports.routers.queryOrThrow
import pt.isel.ls.sports.routers.tokenOrThrow
import pt.isel.ls.sports.toIntOrThrow

/**
 * Represents the activity's router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class ActivitiesRouter(private val services: SportsServices) {

    companion object {

        /**
         * Returns the router routes.
         * @param services router services
         * @return router routes
         */
        fun routes(services: SportsServices) = ActivitiesRouter(services).routes
    }

    val routes = routes(
        "/" bind POST to ::createActivity,
        "/{id}" bind GET to ::getActivity,
        "/{id}" bind DELETE to ::deleteActivity,
        "/search" bind GET to ::searchActivities
    )

    /**
     * Creates an activity.
     * @param request activity creation HTTP request
     * @return activity creation HTTP response
     */
    private fun createActivity(request: Request): Response = runCatching {
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
        val aid = request.pathOrThrow("id").toIntOrThrow { "Invalid Activity Id" }

        services.deleteActivity(aid)

        return Response(OK).json(MessageResponse("Activity deleted"))
    }.getOrElse(::getErrorResponse)

    // TODO: 24/03/2022 Add skip and limit for pagination with search
    /**
     * Gets all activities, given some parameters in the request query.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun searchActivities(request: Request): Response = runCatching {
        val sid = request.queryOrThrow("sid").toIntOrThrow { "Invalid Sport Id" }
        val orderBy = request.queryOrThrow("orderBy")

        val date = request.query("date")
        val rid = request.query("rid")?.toInt()

        val activities = services.getActivities(sid, orderBy, date, rid)

        return Response(OK).json(activities)
    }.getOrElse(::getErrorResponse)
}
