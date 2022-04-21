package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.database.sections.routes.RoutesResponse

/**
 * Represents a response with routes.
 *
 * @property routes list of routes
 */
@Serializable
data class RoutesResponseDTO(val routes: List<RouteDTO>, val totalCount: Int) {

    companion object {
        operator fun invoke(routesResponse: RoutesResponse): RoutesResponseDTO {
            val routesDTOs = routesResponse.routes.map { RouteDTO(it) }

            return RoutesResponseDTO(routesDTOs, routesResponse.totalCount)
        }
    }
}
