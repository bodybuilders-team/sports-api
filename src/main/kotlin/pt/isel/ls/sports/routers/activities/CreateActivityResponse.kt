package pt.isel.ls.sports.routers.activities

import kotlinx.serialization.Serializable


/**
 * Represents an activity creation response.
 *
 * @property aid of the created activity
 */
@Serializable
data class CreateActivityResponse(val aid: Int)
