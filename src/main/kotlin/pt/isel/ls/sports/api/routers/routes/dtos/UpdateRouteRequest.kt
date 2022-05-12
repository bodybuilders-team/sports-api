package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a route update request.
 *
 * @property startLocation
 * @property endLocation
 */
@Serializable
data class UpdateRouteRequest(
    val startLocation: String? = null,
    val endLocation: String? = null,
    val distance: Double? = null
)
