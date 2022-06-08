package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.database.sections.activities.ActivitiesUsersResponse
import pt.isel.ls.sports.database.sections.users.UsersResponse

/**
 * Represents a response with activities users.
 *
 * The number of users depends on pagination.
 * [totalCount] represents the total number of users that could have been retrieved regardless of pagination,
 * and as such is used for calculation of page numbers.
 *
 * @property activitiesUsers list of users
 * @property totalCount total number of users
 */
@Serializable
data class ActivitiesUsersResponseDTO(val activitiesUsers: List<ActivitiesUserDTO>, val totalCount: Int) {
    companion object {

        /**
         * Converts a [UsersResponse] to a [ActivitiesUsersResponseDTO].
         *
         * @param usersResponse response to be converted
         * @return [ActivitiesUsersResponseDTO] representation of the response
         */
        operator fun invoke(usersResponse: ActivitiesUsersResponse): ActivitiesUsersResponseDTO {
            val activitiesUsersDTO = usersResponse.activitiesUsers.map { ActivitiesUserDTO(it) }

            return ActivitiesUsersResponseDTO(activitiesUsersDTO, usersResponse.totalCount)
        }
    }
}
