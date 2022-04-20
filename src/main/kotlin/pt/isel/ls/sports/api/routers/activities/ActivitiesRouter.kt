package pt.isel.ls.sports.api.routers.activities

import kotlinx.datetime.toLocalDate
import org.http4k.core.Method.DELETE
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
import pt.isel.ls.sports.api.routers.activities.dtos.CreateActivityRequest
import pt.isel.ls.sports.api.routers.activities.dtos.CreateActivityResponse
import pt.isel.ls.sports.api.routers.activities.dtos.DeleteActivitiesRequest
import pt.isel.ls.sports.api.routers.users.dtos.UserDTO
import pt.isel.ls.sports.api.routers.users.dtos.UsersResponse
import pt.isel.ls.sports.api.utils.MessageResponse
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.pathOrThrow
import pt.isel.ls.sports.api.utils.queryOrThrow
import pt.isel.ls.sports.api.utils.runAndCatch
import pt.isel.ls.sports.api.utils.tokenOrThrow
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.services.sections.ActivitiesServices
import pt.isel.ls.sports.utils.toDuration
import pt.isel.ls.sports.utils.toIntOrThrow

/**
 * Represents the activity's router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class ActivitiesRouter(private val services: ActivitiesServices) : IRouter {

    companion object : IRouterCompanion<ActivitiesServices> {
        const val DEFAULT_SKIP = 0
        const val DEFAULT_LIMIT = 10

        override fun routes(services: ActivitiesServices) = ActivitiesRouter(services).routes
    }

    override val routes = routes(
        "/" bind POST to ::createActivity,
        "/" bind GET to ::searchActivities,
        "/" bind DELETE to ::deleteActivities,
        "/users" bind GET to ::searchUsersByActivity,
        "/{id}" bind GET to ::getActivity,
        "/{id}" bind DELETE to ::deleteActivity,
    )

    /**
     * Creates an activity.
     *
     * @param request activity creation HTTP request
     * @return HTTP response
     */
    private fun createActivity(request: Request): Response = runAndCatch {
        val token = request.tokenOrThrow()

        val activityReq = request.decodeBodyAs<CreateActivityRequest>()

        val aid = services.createNewActivity(
            token,
            activityReq.date.toLocalDate(),
            activityReq.duration.toDuration(),
            activityReq.sid,
            activityReq.rid
        )

        return Response(CREATED).json(CreateActivityResponse(aid))
    }

    /**
     * Gets a specific activity.
     *
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getActivity(request: Request): Response = runAndCatch {
        val aid = request.pathOrThrow("id").toIntOrThrow { "Invalid Activity Id" }

        val activity = services.getActivity(aid)

        return Response(OK).json(ActivityDTO(activity))
    }

    /**
     * Deletes an activity.
     *
     * @param request HTTP request
     * @return HTTP response
     */
    private fun deleteActivity(request: Request): Response = runAndCatch {
        val token = request.tokenOrThrow()
        val aid = request.pathOrThrow("id").toIntOrThrow { "Invalid Activity Id" }

        services.deleteActivity(token, aid)

        return Response(OK).json(MessageResponse("Activity deleted"))
    }

    /**
     * Deletes a set of activities.
     *
     * @param request HTTP request
     * @return HTTP response
     */
    private fun deleteActivities(request: Request): Response = runAndCatch {
        val token = request.tokenOrThrow()

        val activityIds = request.decodeBodyAs<DeleteActivitiesRequest>().activityIds

        services.deleteActivities(token, activityIds)

        return Response(OK).json(MessageResponse("Activities deleted"))
    }

    /**
     * Searches for all activities that satisfy the given query parameters of the request.
     *
     * @param request HTTP request
     * @return HTTP response
     */
    private fun searchActivities(request: Request): Response = runAndCatch {
        val sid = request.queryOrThrow("sid").toIntOrThrow { "Invalid Sport Id" }
        val orderBy = request.queryOrThrow("orderBy")
        val date = request.query("date")
        val rid = request.query("rid")?.toInt()
        val skip = request.query("skip")?.toInt() ?: DEFAULT_SKIP
        val limit = request.query("limit")?.toInt() ?: DEFAULT_LIMIT

        if (date != null && !ActivityDTO.isValidDate(date))
            throw AppException.InvalidArgument("Date must be in the format yyyy-mm-dd")

        val dateLDT = date?.toLocalDate()

        val activities = services.searchActivities(sid, orderBy, dateLDT, rid, skip, limit)

        return Response(OK).json(ActivitiesResponse(activities.map { ActivityDTO(it) }))
    }

    /**
     * Searches for all users that have an activity that satisfies the given query parameters of the request.
     *
     * @param request HTTP request
     * @return HTTP response
     */
    private fun searchUsersByActivity(request: Request): Response = runAndCatch {
        val sid = request.queryOrThrow("sid").toIntOrThrow { "Invalid Sport Id" }
        val rid = request.queryOrThrow("rid").toIntOrThrow { "Invalid Route Id" }
        val skip = request.query("skip")?.toInt() ?: DEFAULT_SKIP
        val limit = request.query("limit")?.toInt() ?: DEFAULT_LIMIT

        val users = services.searchUsersByActivity(sid, rid, skip, limit)

        return Response(OK).json(UsersResponse(users.map { UserDTO(it) }))
    }
}
