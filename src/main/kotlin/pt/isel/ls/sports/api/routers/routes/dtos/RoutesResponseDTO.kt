package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.database.sections.routes.RoutesResponse

/**
 * Represents a response with routes.
 *
 * The number of routes depends on pagination.
 * [totalCount] represents the total number of routes that could have been retrieved regardless of pagination,
 * and as such is used for calculation of page numbers.
 *
 * @property routes list of routes
 * @property totalCount total number of routes
 */
@Serializable
data class RoutesResponseDTO(val routes: List<RouteDTO>, val totalCount: Int) {
    companion object {
        /**
         * Converts a [RoutesResponse] to a [RoutesResponseDTO].
         *
         * @param routesResponse response to be converted
         * @return [RoutesResponseDTO] representation of the response
         */
        operator fun invoke(routesResponse: RoutesResponse): RoutesResponseDTO {
            val routesDTOs = routesResponse.routes.map { RouteDTO(it) }

            return RoutesResponseDTO(routesDTOs, routesResponse.totalCount)
        }
    }
}
