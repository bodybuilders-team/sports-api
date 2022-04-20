package pt.isel.ls.sports.api.routers.users.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a response with users.
 *
 * @property users list of users
 */
@Serializable
data class UsersResponse(val users: List<UserDTO>)
