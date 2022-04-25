package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.database.sections.sports.SportsResponse

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
@Serializable
data class SportsResponseDTO(val sports: List<SportDTO>, val totalCount: Int) {
    companion object {
        /**
         * Converts a [SportsResponse] to a [SportsResponseDTO].
         *
         * @param sportsResponse response to be converted
         * @return [SportsResponseDTO] representation of the response
         */
        operator fun invoke(sportsResponse: SportsResponse): SportsResponseDTO {
            val sportsDTOs = sportsResponse.sports.map { SportDTO(it) }

            return SportsResponseDTO(sportsDTOs, sportsResponse.totalCount)
        }
    }
}
