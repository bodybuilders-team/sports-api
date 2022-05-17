package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a user login response.
 *
 * @property token token of the user
 */
@Serializable
data class LoginUserResponse(val token: String)
