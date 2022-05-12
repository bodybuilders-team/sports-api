package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.database.sections.activities.ActivitiesResponse

/**
 * Represents a response with activities.
 *
 * The number of activities depends on pagination.
 * [totalCount] represents the total number of activities that could have been retrieved regardless of pagination,
 * and as such is used for calculation of page numbers.
 *
 * @property activities list of activities
 * @property totalCount total number of activities
 */
@Serializable
data class ActivitiesResponseDTO(val activities: List<ActivityDTO>, val totalCount: Int) {
    companion object {

        /**
         * Converts an [ActivitiesResponse] to an [ActivitiesResponseDTO].
         *
         * @param activitiesResponse response to be converted
         * @return [ActivitiesResponseDTO] representation of the response
         */
        operator fun invoke(activitiesResponse: ActivitiesResponse): ActivitiesResponseDTO {
            val activitiesDTOs = activitiesResponse.activities.map { ActivityDTO(it) }

            return ActivitiesResponseDTO(activitiesDTOs, activitiesResponse.totalCount)
        }
    }
}
