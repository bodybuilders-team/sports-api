package pt.isel.ls.sports.database.sections.sports

import pt.isel.ls.sports.domain.Sport

/**
 * Represents a response with sports.
 *
 * @property sports list of sports
 */
data class SportsResponse(val sports: List<Sport>, val totalCount: Int)
