package pt.isel.ls.sports.routers.activities

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
import pt.isel.ls.sports.AppError
import pt.isel.ls.sports.SportsServices
import pt.isel.ls.sports.authenticate
import pt.isel.ls.sports.getErrorResponse


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
		"/activities" bind POST to ::createActivity,
		"/activities/{id}" bind GET to ::getActivity,
		"/activities/{id}" bind DELETE to ::deleteActivity,
		"/activities/search" bind GET to ::searchActivities
	)


	/**
	 * Creates an activity.
	 * @param request activity creation HTTP request
	 * @return activity creation HTTP response
	 */
	private fun createActivity(request: Request): Response = runCatching {
		val token = request.header("Authorization")
			?: return AppError.noCredentials().toResponse()
		authenticate(token)

		val activityReq = Json.decodeFromString<CreateActivityRequest>(request.bodyString())
		val aid = services.createNewActivity(
			token,
			activityReq.date,
			activityReq.duration,
			activityReq.sid,
			activityReq.rid
		)

		return Response(CREATED)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(CreateActivityResponse(aid)))

	}.getOrElse {
		return getErrorResponse(it)
	}


	/**
	 * Gets a specific activity.
	 * @param request HTTP request
	 * @return HTTP response
	 */
	private fun getActivity(request: Request): Response = runCatching {
		val aid = request.path("id")?.toInt()
			?: return AppError.badRequest("Invalid Activity Id").toResponse()

		val activity = services.getActivity(aid)

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(activity))

	}.getOrElse {
		return getErrorResponse(it)
	}


	/**
	 * Deletes an activity.
	 * @param request HTTP request
	 * @return HTTP response
	 */
	private fun deleteActivity(request: Request): Response = runCatching {
		val aid = request.path("id")?.toInt()
			?: return AppError.badRequest("Invalid Activity Id").toResponse()

		services.deleteActivity(aid)

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(DeleteActivityResponse("Activity deleted")))

	}.getOrElse {
		return getErrorResponse(it)
	}


	// TODO: 24/03/2022 Add skip and limit for pagination with search
	/**
	 * Gets all activities, given some parameters in the request query.
	 * @param request HTTP request
	 * @return HTTP response
	 */
	private fun searchActivities(request: Request): Response = runCatching {
		val sid = request.query("sid")?.toInt()
			?: return AppError.badRequest("Sport Id required").toResponse()
		val orderBy = request.query("orderBy")
			?: return AppError.badRequest("Order By required").toResponse()
		val date = request.query("date")
		val rid = request.query("rid")?.toInt()

		val activities = services.getActivities(sid, orderBy, date, rid)

		return Response(OK)
			.header("Content-Type", "application/json")
			.body(Json.encodeToString(activities))

	}.getOrElse {
		return getErrorResponse(it)
	}
}
