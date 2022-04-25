package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.services.sections.users.CreateUserResponse

/**
 * Represents a user creation response.
 *
 * @property token user's token
 * @property uid user's unique identifier
 */
@Serializable
data class CreateUserResponseDTO(val token: String, val uid: Int) {
    companion object {
        /**
         * Converts a [CreateUserResponse] to a [CreateUserResponseDTO].
         *
         * @param createUserResponse the response to be converted
         * @return [CreateUserResponseDTO] representation of the response
         */
        operator fun invoke(createUserResponse: CreateUserResponse) =
            CreateUserResponseDTO(createUserResponse.token, createUserResponse.uid)
    }
}
