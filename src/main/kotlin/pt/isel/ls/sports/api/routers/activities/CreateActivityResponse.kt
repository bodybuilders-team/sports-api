package pt.isel.ls.sports.api.routers.activities

import kotlinx.serialization.Serializable

/**
 * Represents an activity creation response.
 *
 * @property aid of the created activity
 */
@Serializable
data class CreateActivityResponse(val aid: Int)
