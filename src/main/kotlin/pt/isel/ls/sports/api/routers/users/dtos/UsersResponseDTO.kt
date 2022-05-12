package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.database.sections.users.UsersResponse

/**
 * Represents a response with users.
 *
 * The number of users depends on pagination.
 * [totalCount] represents the total number of users that could have been retrieved regardless of pagination,
 * and as such is used for calculation of page numbers.
 *
 * @property users list of users
 * @property totalCount total number of users
 */
@Serializable
data class UsersResponseDTO(val users: List<UserDTO>, val totalCount: Int) {
    companion object {

        /**
         * Converts a [UsersResponse] to a [UsersResponseDTO].
         *
         * @param usersResponse response to be converted
         * @return [UsersResponseDTO] representation of the response
         */
        operator fun invoke(usersResponse: UsersResponse): UsersResponseDTO {
            val usersDTO = usersResponse.users.map { UserDTO(it) }

            return UsersResponseDTO(usersDTO, usersResponse.totalCount)
        }
    }
}
