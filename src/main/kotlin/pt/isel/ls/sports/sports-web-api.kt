package pt.isel.ls.sports

import org.http4k.core.HttpHandler
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import pt.isel.ls.sports.routes.*


class SportsWebApi(services: SportsServices) {

    private val routes: RoutingHttpHandler

    init {
        val apiRoutes =
                UserRouter.routes(services) bind
                    RouteRouter.routes(services) bind
                    SportsRouter.routes(services) bind
                    ActivitiesRouter.routes(services)

        routes = "/api" bind apiRoutes
    }

    fun getApp(): HttpHandler {
        return routes
    }

}



