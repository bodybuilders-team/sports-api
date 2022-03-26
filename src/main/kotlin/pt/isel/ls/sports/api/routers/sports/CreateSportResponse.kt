package pt.isel.ls.sports.api.routers.sports

import kotlinx.serialization.Serializable

/**
 * Represents a Sport creation response.
 *
 * @property sid of the created sport
 */
@Serializable
data class CreateSportResponse(val sid: Int)