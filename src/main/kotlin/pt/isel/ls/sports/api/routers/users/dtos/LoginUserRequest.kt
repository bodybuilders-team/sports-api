package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a user login request.
 *
 * @property email email of the user
 * @property password password of the user
 */
@Serializable
data class LoginUserRequest(val email: String, val password: String)
