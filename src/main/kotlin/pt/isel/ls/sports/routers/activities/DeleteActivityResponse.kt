package pt.isel.ls.sports.routers.activities

import kotlinx.serialization.Serializable


/**
 * Represents an activity delete response.
 *
 * @property message response message
 */
@Serializable
data class DeleteActivityResponse(val message: String)
