package pt.isel.ls.sports

import org.http4k.core.Filter
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.sports.api.AppWebApi
import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.services.AppServices
import pt.isel.ls.sports.utils.Logger

/**
 * Represents a Sports API application server.
 * @property port port where the server is running
 * @property database application database
 * @property server HTTP4K server
 */
class AppServer(private val port: Int, private val database: AppDB) {

    private val server: Http4kServer

    init {
        val services = AppServices(database)
        val webApi = AppWebApi(services)

        // Need Cross origin filter to allow requests from other domains (in this case the
        // openApi documentation in the intellij plugin)
        val corsFilter = ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive)

        // Logs all requests
        val logRequestFilter = Filter { next ->
            { request ->
                Logger.logRequest(request)
                next(request)
            }
        }

        val app = routes(
            "/api" bind webApi.getHandler(),
        )
            .withFilter(corsFilter)
            .withFilter(logRequestFilter)

        server = app.asServer(Jetty(port))
    }

    /**
     * Starts the server.
     */
    fun start() {
        server.start()
        Logger.info("Starting server on port $port")
    }

    /**
     * Stops the server.
     */
    fun stop() {
        server.stop()
        Logger.info("stopping server")
    }
}
