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

        const val MIN_LOCATION_LENGTH = 3
        const val MAX_LOCATION_LENGTH = 250

        /**
         * Checks if a location is valid.
         *
         * @param location location to check
         * @return true if the location is valid, false otherwise
         */
        fun isValidLocation(location: String) =
            location.length in MIN_LOCATION_LENGTH..MAX_LOCATION_LENGTH

        /**
         * Checks if a distance is valid.
         *
         * @param distance distance to check
         * @return true if the distance is valid, false otherwise
         */
        fun isValidDistance(distance: Double) =
            distance >= 0
    }

    init {
        require(isValidId(id)) { "Invalid route id: $id" }
        require(isValidLocation(startLocation)) { "Invalid start location: $startLocation" }
        require(isValidLocation(endLocation)) { "Invalid end location: $endLocation" }
        require(isValidDistance(distance)) { "Invalid distance: $distance" }
        require(isValidId(uid)) { "Invalid sport id: $uid" }
    }
}
