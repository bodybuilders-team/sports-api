package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UpdateActivityResponse(
    val modified: Boolean
)
