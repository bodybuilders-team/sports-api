package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSportResponse(
    val modified: Boolean
)
