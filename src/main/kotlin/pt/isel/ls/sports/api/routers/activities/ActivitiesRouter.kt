package pt.isel.ls.sports.api.routers.activities

import kotlinx.datetime.toLocalDate
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.PATCH
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.sports.api.routers.IRouter
import pt.isel.ls.sports.api.routers.activities.dtos.ActivitiesResponseDTO
import pt.isel.ls.sports.api.routers.activities.dtos.ActivitiesUsersResponseDTO
import pt.isel.ls.sports.api.routers.activities.dtos.ActivityDTO
import pt.isel.ls.sports.api.routers.activities.dtos.CreateActivityRequest
import pt.isel.ls.sports.api.routers.activities.dtos.CreateActivityResponse
import pt.isel.ls.sports.api.routers.activities.dtos.DeleteActivitiesRequest
import pt.isel.ls.sports.api.routers.activities.dtos.UpdateActivityRequest
import pt.isel.ls.sports.api.routers.activities.dtos.UpdateActivityResponse
import pt.isel.ls.sports.api.utils.MessageResponse
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.errors.runAndCatch
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.pathOrThrow
import pt.isel.ls.sports.api.utils.queryOrThrow
import pt.isel.ls.sports.api.utils.toIntOrThrow
import pt.isel.ls.sports.api.utils.tokenOrThrow
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.services.sections.activities.ActivitiesServices
import pt.isel.ls.sports.utils.toDuration

/**
 * Represents the activity's router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
@Suppress("DuplicatedCode")
class ActivitiesRouter(private val services: ActivitiesServices) : IRouter {
    companion object {
        private const val DEFAULT_SKIP = 0
        private const val DEFAULT_LIMIT = 10

        /**
         * Returns the activities' router's routes.
         *
         * @param services activities services
         * @return activities router's routes
         */
        fun routes(services: ActivitiesServices) = ActivitiesRouter(services).routes
    }

    override val routes = routes(
        "/" bind POST to ::createActivity,
        "/" bind GET to ::searchActivities,
        "/" bind DELETE to ::deleteActivities,
        "/users" bind GET to ::searchUsersByActivity,
        "/{id}" bind PATCH to ::updateActivity,
        "/{id}" bind GET to ::getActivity,
        "/{id}" bind DELETE to ::deleteActivity,
    )

    /**
     * Creates an activity.
     *
     * @param request HTTP request containing a body that follows the [CreateActivityRequest] format
     * @return HTTP response containing a body that follows the [CreateActivityResponse] format
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
     * Updates an activity.
     *
     * @param request HTTP request containing a body that follows the [UpdateActivityRequest] format
     * @return HTTP response containing a body that follows the [UpdateActivityResponse] format
     */
    private fun updateActivity(request: Request): Response = runAndCatch {
        val token = request.tokenOrThrow()
        val id = request.pathOrThrow("id").toIntOrThrow { "Invalid Activity Id" }

        val routeRequest = request.decodeBodyAs<UpdateActivityRequest>()
        val modified = services.updateActivity(
            id,
            token,
            routeRequest.date?.toLocalDate(),
            routeRequest.duration?.toDuration(),
            routeRequest.sid,
            routeRequest.rid
        )

        return Response(OK).json(UpdateActivityResponse(modified))
    }

    /**
     * Gets a specific activity.
     *
     * @param request HTTP request
     * @return HTTP response containing a body that follows the [ActivityDTO] format
     */
    private fun getActivity(request: Request): Response = runAndCatch {
        val id = request.pathOrThrow("id").toIntOrThrow { "Invalid Activity Id" }

        val activity = services.getActivity(id)

        return Response(OK).json(ActivityDTO(activity))
    }

    /**
     * Deletes an activity.
     *
     * @param request HTTP request
     * @return HTTP response containing a body that follows the [MessageResponse] format
     */
    private fun deleteActivity(request: Request): Response = runAndCatch {
        val token = request.tokenOrThrow()
        val id = request.pathOrThrow("id").toIntOrThrow { "Invalid Activity Id" }

        services.deleteActivity(token, id)

        return Response(OK).json(MessageResponse("Activity deleted"))
    }

    /**
     * Deletes a set of activities.
     *
     * @param request HTTP request containing a body that follows the [DeleteActivitiesRequest] format
     * @return HTTP response containing a body that follows the [MessageResponse] format
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
     * @return HTTP response containing a body that follows the [ActivitiesResponseDTO] format
     */
    private fun searchActivities(request: Request): Response = runAndCatch {
        val sid = request.queryOrThrow("sid").toIntOrThrow { "Invalid Sport Id" }
        val orderBy = request.queryOrThrow("orderBy")
        val date = request.query("date")
        val rid = request.query("rid")?.toIntOrThrow { "Invalid Route Id" }
        val skip = request.query("skip")?.toIntOrThrow { "Invalid skip" } ?: DEFAULT_SKIP
        val limit = request.query("limit")?.toIntOrThrow { "Invalid limit" } ?: DEFAULT_LIMIT

        if (date != null && !ActivityDTO.isValidDate(date))
            throw InvalidArgumentException("Date must be in the format yyyy-mm-dd")

        val dateLDT = date?.toLocalDate()

        val activitiesResponse = services.searchActivities(sid, orderBy, dateLDT, rid, skip, limit)

        return Response(OK).json(ActivitiesResponseDTO(activitiesResponse))
    }

    /**
     * Searches for all users that have an activity that satisfies the given query parameters of the request.
     *
     * @param request HTTP request
     * @return HTTP response containing a body that follows the [ActivitiesUsersResponseDTO] format
     */
    private fun searchUsersByActivity(request: Request): Response = runAndCatch {
        val sid = request.queryOrThrow("sid").toIntOrThrow { "Invalid Sport Id" }
        val rid = request.query("rid")?.toIntOrThrow { "Invalid Route Id" }
        val skip = request.query("skip")?.toIntOrThrow { "Invalid skip" } ?: DEFAULT_SKIP
        val limit = request.query("limit")?.toIntOrThrow { "Invalid limit" } ?: DEFAULT_LIMIT

        val usersResponse = services.searchUsersByActivity(sid, rid, skip, limit)

        return Response(OK).json(ActivitiesUsersResponseDTO(usersResponse))
    }
}
