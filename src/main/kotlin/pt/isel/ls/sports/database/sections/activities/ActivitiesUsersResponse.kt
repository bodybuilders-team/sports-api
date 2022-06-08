package pt.isel.ls.sports.database.sections.activities

/**
 * Represents a response with activities users.
 *
 * The number of users depends on pagination.
 * [totalCount] represents the total number of users that could have been retrieved regardless of pagination,
 * and as such is used for calculation of page numbers.
 *
 * @property activitiesUsers list of users
 * @property totalCount total number of users
 */
data class ActivitiesUsersResponse(val activitiesUsers: List<ActivitiesUser>, val totalCount: Int)
