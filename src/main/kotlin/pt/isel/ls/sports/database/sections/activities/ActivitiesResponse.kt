package pt.isel.ls.sports.database.sections.activities

import pt.isel.ls.sports.domain.Activity

/**
 * Represents a response with activities.
 *
 * @property activities list of activities
 */
data class ActivitiesResponse(val activities: List<Activity>, val totalCount: Int)
