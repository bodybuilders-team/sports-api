package pt.isel.ls.sports.api.routers.sports

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.sports.api.routers.IRouter
import pt.isel.ls.sports.api.routers.activities.dtos.ActivitiesResponseDTO
import pt.isel.ls.sports.api.routers.sports.dtos.CreateSportRequestDTO
import pt.isel.ls.sports.api.routers.sports.dtos.CreateSportResponseDTO
import pt.isel.ls.sports.api.routers.sports.dtos.SportDTO
import pt.isel.ls.sports.api.routers.sports.dtos.SportsResponseDTO
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.pathOrThrow
import pt.isel.ls.sports.api.utils.runAndCatch
import pt.isel.ls.sports.api.utils.tokenOrThrow
import pt.isel.ls.sports.services.sections.sports.SportsServices
import pt.isel.ls.sports.utils.toIntOrThrow

/**
 * Represents the sports' router for the Web API.
 *
 * @property services router services
 * @property routes router routes
 */
class SportsRouter(private val services: SportsServices) : IRouter {

    companion object {
        const val DEFAULT_SKIP = 0
        const val DEFAULT_LIMIT = 10

        fun routes(services: SportsServices) = SportsRouter(services).routes
    }

    override val routes = routes(
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
    private fun createSport(request: Request): Response = runAndCatch {
        val token = request.tokenOrThrow()

        val sportRequest = request.decodeBodyAs<CreateSportRequestDTO>()
        val sid = services.createNewSport(
            token, sportRequest.name,
            sportRequest.description
        )

        return Response(CREATED).json(CreateSportResponseDTO(sid))
    }

    /**
     * Gets all sports.
     * @param request HTTP request
     * @return HTTP response
     */
    @Suppress("UNUSED_PARAMETER")
    private fun getSports(request: Request): Response = runAndCatch {
        val skip = request.query("skip")?.toIntOrThrow() ?: DEFAULT_SKIP
        val limit = request.query("limit")?.toIntOrThrow() ?: DEFAULT_LIMIT

        val sportsResponse = services.getAllSports(skip, limit)

        return Response(OK).json(SportsResponseDTO(sportsResponse))
    }

    /**
     * Gets a specific sport.
     * @param request HTTP request
     * @return HTTP response
     */
    private fun getSport(request: Request): Response = runAndCatch {
        val sid = request.pathOrThrow("id").toIntOrThrow { "Invalid Sport Id" }

        val sport = services.getSport(sid)

        return Response(OK).json(SportDTO(sport))
    }

    private fun getSportActivities(request: Request): Response = runAndCatch {
        val sid = request.pathOrThrow("id").toIntOrThrow { "Invalid Sport Id" }
        val skip = request.query("skip")?.toIntOrThrow() ?: DEFAULT_SKIP
        val limit = request.query("limit")?.toIntOrThrow() ?: DEFAULT_LIMIT

        val activitiesResponse = services.getSportActivities(sid, skip, limit)

        return Response(OK).json(ActivitiesResponseDTO(activitiesResponse))
    }
}
