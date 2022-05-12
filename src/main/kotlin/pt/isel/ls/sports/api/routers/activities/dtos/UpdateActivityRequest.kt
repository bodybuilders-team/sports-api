package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UpdateActivityRequest(
    val date: String
)
