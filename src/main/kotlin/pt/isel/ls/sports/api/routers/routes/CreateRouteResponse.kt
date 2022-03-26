package pt.isel.ls.sports.api.routers.routes

import kotlinx.serialization.Serializable

/**
 * Represents a Route creation response.
 *
 * @property rid of the created route
 */
@Serializable
data class CreateRouteResponse(val rid: Int)