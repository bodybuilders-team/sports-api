package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.services.utils.isValidId

/**
 * User data transfer object representation.
 *
 * @property id user's unique identifier
 * @property name name of the user
 * @property email email of the user
 */
@Serializable
data class UserDTO(
    val id: Int,
    val name: String,
    val email: String
) {
    companion object {

        /**
         * Converts a [User] to a [UserDTO].
         *
         * @param user user to be converted
         * @return [UserDTO] representation of the user
         */
        operator fun invoke(user: User): UserDTO =
            UserDTO(user.id, user.name, user.email)
    }

    init {
        require(isValidId(id)) { "Invalid user id: $id" }
        require(User.isValidName(name)) { "Invalid user name: $name" }
        require(User.isValidEmail(email)) { "Invalid user email: $email" }
    }
}
