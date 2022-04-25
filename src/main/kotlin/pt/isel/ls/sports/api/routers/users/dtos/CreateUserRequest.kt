package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a user creation request.
 *
 * @property name name of the user
 * @property email email of the user
 */
@Serializable
data class CreateUserRequest(val name: String, val email: String)
