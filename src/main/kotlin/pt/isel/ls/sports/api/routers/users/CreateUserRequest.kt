package pt.isel.ls.sports.api.routers.users

import kotlinx.serialization.Serializable

/**
 * Represents a User creation request.
 *
 * @property name username
 * @property email user email
 */
@Serializable
data class CreateUserRequest(val name: String, val email: String)
