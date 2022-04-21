package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.database.sections.sports.SportsResponse

/**
 * Represents a response with sports.
 *
 * @property sports list of sports
 */
@Serializable
data class SportsResponseDTO(val sports: List<SportDTO>, val totalCount: Int) {

    companion object {
        operator fun invoke(sportsResponse: SportsResponse): SportsResponseDTO {
            val sportsDTOs = sportsResponse.sports.map { SportDTO(it) }

            return SportsResponseDTO(sportsDTOs, sportsResponse.totalCount)
        }
    }
}
