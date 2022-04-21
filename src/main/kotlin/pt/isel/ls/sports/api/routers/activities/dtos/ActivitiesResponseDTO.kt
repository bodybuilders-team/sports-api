package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.database.sections.activities.ActivitiesResponse

/**
 * Represents a response with activities.
 *
 * @property activities list of activities
 */
@Serializable
data class ActivitiesResponseDTO(val activities: List<ActivityDTO>, val totalCount: Int) {
    companion object {
        operator fun invoke(activitiesResponse: ActivitiesResponse): ActivitiesResponseDTO {
            val activitiesDTOs = activitiesResponse.activities.map { ActivityDTO(it) }

            return ActivitiesResponseDTO(activitiesDTOs, activitiesResponse.totalCount)
        }
    }
}
