package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.services.sections.users.LoginUserResponse

/**
 * Represents a user login response.
 *
 * @property token token of the user
 * @property uid user id
 */
@Serializable
data class LoginUserResponseDTO(val token: String, val uid: Int) {
    companion object {

        operator fun invoke(loginUserResponse: LoginUserResponse): LoginUserResponseDTO {
            return LoginUserResponseDTO(loginUserResponse.token, loginUserResponse.uid)
        }
    }
}
