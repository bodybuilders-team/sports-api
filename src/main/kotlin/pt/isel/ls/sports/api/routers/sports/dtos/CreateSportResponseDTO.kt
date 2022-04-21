package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable

/**
 * Represents a Sport creation response.
 *
 * @property sid of the created sport
 */
@Serializable
data class CreateSportResponseDTO(val sid: Int)
