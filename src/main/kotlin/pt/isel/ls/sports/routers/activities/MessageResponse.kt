package pt.isel.ls.sports.routers.activities

import kotlinx.serialization.Serializable

/**
 * Represents a message response.
 *
 * @property message response message
 */
@Serializable
data class MessageResponse(val message: String)
