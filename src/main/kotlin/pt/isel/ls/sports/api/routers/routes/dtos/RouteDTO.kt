package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.services.utils.isValidId

/**
 * Route data transfer object representation.
 *
 * @property id route's unique identifier
 * @property startLocation start location of the route
 * @property endLocation end location of the route
 * @property distance distance to travel between [startLocation] and [endLocation] in kilometers
 * @property uid unique identifier of the user who created the route
 */
@Serializable
data class RouteDTO(
    val id: Int,
    val startLocation: String,
    val endLocation: String,
    val distance: Double,
    val uid: Int
) {
    companion object {

        /**
         * Converts a [Route] to a [RouteDTO].
         *
         * @param route route to be converted
         * @return [RouteDTO] representation of the route
         */
        operator fun invoke(route: Route): RouteDTO =
            RouteDTO(
                route.id,
                route.startLocation,
                route.endLocation,
                route.distance,
                route.uid
            )
    }

    init {
        require(isValidId(id)) { "Invalid route id: $id" }
        require(Route.isValidDistance(distance)) { "Invalid distance: $distance" }
        require(isValidId(uid)) { "Invalid sport id: $uid" }
    }
}
