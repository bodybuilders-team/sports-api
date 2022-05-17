package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a user creation response.
 *
 * @property uid user's unique identifier
 */
@Serializable
data class CreateUserResponse(val uid: Int)
