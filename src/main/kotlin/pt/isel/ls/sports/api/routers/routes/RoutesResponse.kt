package pt.isel.ls.sports.api.routers.routes

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.domain.Route

@Serializable
data class RoutesResponse(val routes: List<Route>)
