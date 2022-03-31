package pt.isel.ls.sports.api.routers.users

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.domain.User

@Serializable
data class UsersResponse(val users: List<User>)
