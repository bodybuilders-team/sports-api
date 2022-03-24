package pt.isel.ls.sports

import org.http4k.core.HttpHandler
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import pt.isel.ls.sports.routers.activities.ActivitiesRouter
import pt.isel.ls.sports.routers.routes.RoutesRouter
import pt.isel.ls.sports.routers.sports.SportsRouter
import pt.isel.ls.sports.routers.users.UsersRouter


/**
 * Represents the Sports API Web API.
 * @param services API services
 * @property routes API routes
 */
class SportsWebApi(services: SportsServices) {

	private val routes: RoutingHttpHandler

	init {
		val apiRoutes =
			UsersRouter.routes(services) bind
					RoutesRouter.routes(services) bind
					SportsRouter.routes(services) bind
					ActivitiesRouter.routes(services)

		routes = "/api" bind apiRoutes
	}

	/**
	 * Gets the Web API application.
	 * @return Web API routes
	 */
	fun getApp(): HttpHandler = routes
}
