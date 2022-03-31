package pt.isel.ls.sports.api

import org.http4k.core.HttpHandler
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.sports.api.routers.activities.ActivitiesRouter
import pt.isel.ls.sports.api.routers.routes.RoutesRouter
import pt.isel.ls.sports.api.routers.sports.SportsRouter
import pt.isel.ls.sports.api.routers.users.UsersRouter
import pt.isel.ls.sports.services.AppServices

/**
 * Represents the Sports API Web API.
 * @param services API services
 * @property routes API routes
 */
class AppWebApi(services: AppServices) {

    private val routes: RoutingHttpHandler

    init {
        val apiRoutes = routes(
            "/users" bind UsersRouter.routes(services.users),
            "/routes" bind RoutesRouter.routes(services.routes),
            "/sports" bind SportsRouter.routes(services.sports),
            "/activities" bind ActivitiesRouter.routes(services.activities)
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
