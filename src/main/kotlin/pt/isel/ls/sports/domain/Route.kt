@file:Suppress(
    "BooleanMethodIsAlwaysInverted", "BooleanMethodIsAlwaysInverted", "BooleanMethodIsAlwaysInverted",
    "BooleanMethodIsAlwaysInverted", "BooleanMethodIsAlwaysInverted", "BooleanMethodIsAlwaysInverted"
)

package pt.isel.ls.sports.domain

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.services.isValidId

/**
 * Route  representation.
 *
 * @property id route unique identifier
 * @property start_location start location of the route
 * @property end_location end location of the route
 * @property distance distance between [start_location] and [end_location] in meters
 * @property uid unique identifier of the user who created the route
 */
@Suppress(
    "BooleanMethodIsAlwaysInverted", "BooleanMethodIsAlwaysInverted", "BooleanMethodIsAlwaysInverted",
    "BooleanMethodIsAlwaysInverted", "BooleanMethodIsAlwaysInverted", "BooleanMethodIsAlwaysInverted"
)
@Serializable
data class Route(
    val id: Int,
    val start_location: String,
    val end_location: String,
    val distance: Double,
    val uid: Int
) {
    companion object {
        fun isValidDistance(distance: Double) =
            distance >= 0
    }

    init {
        require(isValidId(id)) { "Invalid route id: $id" }
        require(isValidDistance(distance)) { "Invalid distance: $distance" }
        require(isValidId(uid)) { "Invalid sport id: $uid" }
    }
}
