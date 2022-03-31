package pt.isel.ls.sports.api

import org.http4k.core.HttpHandler
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.sports.api.routers.activities.ActivitiesRouter
import pt.isel.ls.sports.api.routers.routes.RoutesRouter
import pt.isel.ls.sports.api.routers.sports.SportsRouter
import pt.isel.ls.sports.api.routers.users.UsersRouter
import pt.isel.ls.sports.services.SportsServices

/**
 * Represents the Sports API Web API.
 * @param services API services
 * @property routes API routes
 */
class SportsWebApi(services: SportsServices) {

    private val routes: RoutingHttpHandler

    init {
        val apiRoutes = routes(
            "/users" bind UsersRouter.routes(services),
            "/routes" bind RoutesRouter.routes(services),
            "/sports" bind SportsRouter.routes(services),
            "/activities" bind ActivitiesRouter.routes(services)
        )

        routes = "/api" bind apiRoutes
    }

    /**
     * Gets the Web API application.
     *
     * @return Web API routes
     */
    fun getApp(): HttpHandler = routes
}
