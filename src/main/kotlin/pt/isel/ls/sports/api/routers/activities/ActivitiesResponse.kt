package pt.isel.ls.sports.api.routers.activities

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.domain.Activity

/**
 * Represents a response with activities.
 *
 * @property activities list of activities
 */
@Serializable
data class ActivitiesResponse(val activities: List<Activity>)
