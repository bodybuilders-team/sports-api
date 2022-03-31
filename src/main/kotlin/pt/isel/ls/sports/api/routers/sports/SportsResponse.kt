package pt.isel.ls.sports.api.routers.sports

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.domain.Sport

/**
 * Represents a response with sports.
 *
 * @property sports list of sports
 */
@Serializable
data class SportsResponse(val sports: List<Sport>)
