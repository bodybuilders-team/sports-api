package pt.isel.ls.sports.domain

import kotlinx.serialization.Serializable

/**
 * Route  representation.
 *
 * @property id route unique identifier
 * @property start_location start location of the route
 * @property end_location end location of the route
 * @property distance distance between [start_location] and [end_location] in meters
 * @property uid unique identifier of the user who created the route
 */
@Serializable
data class Route(
    val id: Int,
    val start_location: String,
    val end_location: String,
    val distance: Int,
    val uid: Int
) {
    init {
        require(distance > 0) { "Distance in route must be positive" }
    }
}
