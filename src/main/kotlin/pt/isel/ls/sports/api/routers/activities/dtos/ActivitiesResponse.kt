package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a response with activities.
 *
 * @property activities list of activities
 */
@Serializable
data class ActivitiesResponse(val activities: List<ActivityDTO>)
