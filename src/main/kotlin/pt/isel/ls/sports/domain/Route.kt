package pt.isel.ls.sports.domain

import pt.isel.ls.sports.services.utils.isValidId

/**
 * Route representation.
 *
 * @property id route's unique identifier
 * @property startLocation start location of the route
 * @property endLocation end location of the route
 * @property distance distance to travel between [startLocation] and [endLocation] in kilometers
 * @property uid unique identifier of the user who created the route
 */
data class Route(
    val id: Int,
    val startLocation: String,
    val endLocation: String,
    val distance: Double,
    val uid: Int
) {
    companion object {

        /**
         * Checks if a distance is valid.
         *
         * @param distance distance to check
         * @return true if it's valid
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
