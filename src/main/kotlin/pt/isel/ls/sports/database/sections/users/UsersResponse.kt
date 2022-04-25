package pt.isel.ls.sports.database.sections.users

import pt.isel.ls.sports.domain.User

/**
 * Represents a response with users.
 *
 * The number of users depends on pagination.
 * [totalCount] represents the total number of users that could have been retrieved regardless of pagination,
 * and as such is used for calculation of page numbers.
 *
 * @property users list of users
 * @property totalCount total number of users
 */
data class UsersResponse(val users: List<User>, val totalCount: Int)
