package pt.isel.ls.sports.api.routers

import org.http4k.routing.RoutingHttpHandler

/**
 * Represents a router for the Web API.
 *
 * @property routes router routes
 */
interface IRouter {
    val routes: RoutingHttpHandler
}

interface IRouterCompanion<T> {
    /**
     * Returns the router routes.
     * @param services router services
     * @return router routes
     */
    fun routes(services: T): RoutingHttpHandler
}
