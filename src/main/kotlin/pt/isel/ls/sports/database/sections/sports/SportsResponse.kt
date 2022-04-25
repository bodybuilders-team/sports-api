package pt.isel.ls.sports.database.sections.sports

import pt.isel.ls.sports.domain.Sport

/**
 * Represents a response with sports.
 *
 * The number of sports depends on pagination.
 * [totalCount] represents the total number of sports that could have been retrieved regardless of pagination,
 * and as such is used for calculation of page numbers.
 *
 * @property sports list of sports
 * @property totalCount total number of sports
 */
data class SportsResponse(val sports: List<Sport>, val totalCount: Int)
