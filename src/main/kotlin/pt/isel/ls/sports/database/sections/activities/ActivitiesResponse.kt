package pt.isel.ls.sports.database.sections.activities

import pt.isel.ls.sports.domain.Activity

/**
 * Represents a response with activities.
 *
 * The number of activities depends on pagination.
 * [totalCount] represents the total number of activities that could have been retrieved regardless of pagination,
 * and as such is used for calculation of page numbers.
 *
 * @property activities list of activities
 * @property totalCount total number of activities
 */
data class ActivitiesResponse(val activities: List<Activity>, val totalCount: Int)
