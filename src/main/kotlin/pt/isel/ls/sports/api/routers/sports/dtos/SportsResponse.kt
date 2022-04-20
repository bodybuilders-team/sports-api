package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a response with sports.
 *
 * @property sports list of sports
 */
@Serializable
data class SportsResponse(val sports: List<SportDTO>)
