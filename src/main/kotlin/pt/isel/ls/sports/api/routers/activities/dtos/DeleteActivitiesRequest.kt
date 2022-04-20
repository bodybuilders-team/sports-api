package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a delete activities request.
 *
 * @property activityIds array of identifiers of the activities to be deleted.
 */
@Serializable
data class DeleteActivitiesRequest(
    val activityIds: Set<Int>
)
