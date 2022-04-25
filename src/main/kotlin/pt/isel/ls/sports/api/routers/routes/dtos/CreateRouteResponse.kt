package pt.isel.ls.sports.api.routers.routes.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a route creation response.
 *
 * @property rid route's unique identifier
 */
@Serializable
data class CreateRouteResponse(val rid: Int)
