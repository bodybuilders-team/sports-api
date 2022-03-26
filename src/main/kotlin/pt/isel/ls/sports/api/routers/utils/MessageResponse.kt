package pt.isel.ls.sports.api.routers.utils

import kotlinx.serialization.Serializable

/**
 * Represents a message response.
 *
 * @property message response message
 */
@Serializable
data class MessageResponse(val message: String)
