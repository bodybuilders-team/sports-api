package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable

/**
 * Represents an activities deletion request.
 *
 * @property activityIds set of identifiers of the activities to be deleted
 */
@Serializable
data class DeleteActivitiesRequest(
    val activityIds: Set<Int>
)
