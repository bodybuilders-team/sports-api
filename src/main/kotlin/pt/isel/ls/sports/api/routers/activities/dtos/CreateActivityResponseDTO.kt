package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable

/**
 * Represents an activity creation response.
 *
 * @property aid of the created activity
 */
@Serializable
data class CreateActivityResponseDTO(val aid: Int)
