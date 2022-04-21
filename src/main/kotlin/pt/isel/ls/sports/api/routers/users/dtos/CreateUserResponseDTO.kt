package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.services.sections.users.CreateUserResponse

/**
 * Response for the createUser method of services.
 *
 * @property token user's token
 * @property uid user's unique identifier
 */
@Serializable
data class CreateUserResponseDTO(val token: String, val uid: Int) {
    companion object {
        operator fun invoke(createUserResponse: CreateUserResponse) =
            CreateUserResponseDTO(createUserResponse.token, createUserResponse.uid)
    }
}
