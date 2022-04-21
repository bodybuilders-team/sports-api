package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.database.sections.users.UsersResponse

/**
 * Represents a response with users.
 *
 * @property users list of users
 */
@Serializable
data class UsersResponseDTO(val users: List<UserDTO>, val totalCount: Int) {

    companion object {
        operator fun invoke(usersResponse: UsersResponse): UsersResponseDTO {
            val usersDTO = usersResponse.users.map { UserDTO(it) }

            return UsersResponseDTO(usersDTO, usersResponse.totalCount)
        }
    }
}
