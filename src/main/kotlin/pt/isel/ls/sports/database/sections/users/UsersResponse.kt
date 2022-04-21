package pt.isel.ls.sports.database.sections.users

import pt.isel.ls.sports.domain.User

/**
 * Represents a response with users.
 *
 * @property users list of users
 */
data class UsersResponse(val users: List<User>, val totalCount: Int)
