package pt.isel.ls.sports.domain

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.services.utils.isValidId

/**
 * Route representation.
 *
 * @property id route unique identifier
 * @property startLocation start location of the route
 * @property endLocation end location of the route
 * @property distance distance between [startLocation] and [endLocation] in meters
 * @property uid unique identifier of the user who created the route
 */
@Serializable
data class Route(
    val id: Int,
    val startLocation: String,
    val endLocation: String,
    val distance: Double,
    val uid: Int
) {
    companion object {

        /**
         * Checks if a distance e valid.
         * @param distance distance to check
         * @return true if its valid
         */
        fun isValidDistance(distance: Double) =
            distance >= 0
    }

    init {
        require(isValidId(id)) { "Invalid route id: $id" }
        require(isValidDistance(distance)) { "Invalid distance: $distance" }
        require(isValidId(uid)) { "Invalid sport id: $uid" }
    }
}
