package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable

/**
 * Represents an activity creation response.
 *
 * @property aid activity's unique identifier
 */
@Serializable
data class CreateActivityResponse(val aid: Int)
