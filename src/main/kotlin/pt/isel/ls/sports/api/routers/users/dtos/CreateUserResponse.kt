package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable

/**
 * Response for the createUser method of services.
 *
 * @property token user's token
 * @property uid user's unique identifier
 */
@Serializable
data class CreateUserResponse(val token: String, val uid: Int)
