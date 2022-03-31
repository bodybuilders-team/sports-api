package pt.isel.ls.sports.api.routers.activities

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.domain.Activity

@Serializable
data class ActivitiesResponse(val activities: List<Activity>)
