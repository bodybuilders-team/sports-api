package pt.isel.ls.sports.api.routers.sports

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.sports.api.routers.activities.ActivitiesResponse
import pt.isel.ls.sports.api.routers.activities.ActivityDTO
import pt.isel.ls.sports.api.utils.getErrorResponse
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.pathOrThrow
import pt.isel.ls.sports.api.utils.tokenOrThrow
import pt.isel.ls.sports.services.sections.SportsServices
import pt.isel.ls.sports.utils.logRequest
import pt.isel.ls.sports.utils.toIntOrThrow

/**
 * Represents the sports' router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class SportsRouter(private val services: SportsServices) {

    companion object {

        /**
         * Returns the router routes
         * @param services router services
         * @return router routes
         */
        fun routes(services: SportsServices) = SportsRouter(services).routes
    }

    val routes = routes(
        "/" bind POST to ::createSport,
        "/" bind GET to ::getSports,
        "/{id}" bind GET to ::getSport,
        "/{id}/activities" bind GET to ::getSportActivities,
    )

    /**
     * Creates a sport.
     * @param request sport creation HTTP request
     * @return sport creation HTTP response
     */
    private fun createSport(request: Request): Response = runCatching {
        logRequest(request)
        val token = request.tokenOrThrow()

        val sportRequest = Json.decodeFromString<CreateSportRequest>(request.bodyString())
        val sid = services.createNewSport(
            token, sportRequest.name,
            sportRequest.description
        )

        return Response(CREATED).json(CreateSportResponse(sid))
    }.getOrElse(::getErrorResponse)

    /**
     * Gets all sports.
     * @param request HTTP request
     * @return HTTP response
     */
    @Suppress("UNUSED_PARAMETER")
    private fun getSports(request: Request): Response = runCatching {
        logRequest(request)
        val sports = services.getAllSports()

        return Response(OK).json(SportsResponse(sports.map { SportDTO(it) }))
    }.getOrElse(::getErrorResponse)

    /**
     * Gets a specific sport.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getSport(request: Request): Response = runCatching {
        logRequest(request)
        val sid = request.pathOrThrow("id").toIntOrThrow { "Invalid Sport Id" }

        val sport = services.getSport(sid)

        return Response(OK).json(SportDTO(sport))
    }.getOrElse(::getErrorResponse)

    private fun getSportActivities(request: Request): Response = runCatching {
        logRequest(request)
        val sid = request.pathOrThrow("id").toIntOrThrow { "Invalid Sport Id" }

        val activities = services.getSportActivities(sid)

        return Response(OK).json(ActivitiesResponse(activities.map { ActivityDTO(it) }))
    }.getOrElse(::getErrorResponse)
}
