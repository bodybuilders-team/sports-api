package pt.isel.ls.sports.routers.users

import kotlinx.serialization.Serializable


/**
 * Represents a User creation response.
 *
 * @property uid of the created user
 */
@Serializable
data class CreateUserResponse(val uid: Int)
