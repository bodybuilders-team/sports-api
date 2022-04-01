package pt.isel.ls.sports

import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.sports.api.AppWebApi
import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.services.AppServices

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
        server = webApi.getApp().asServer(Jetty(port))
    }

    /**
     * Starts the server.
     */
    fun start() {
        server.start()
        logger.info("starting server on port $port")
    }

    /**
     * Stops the server.
     */
    fun stop() {
        server.stop()
        logger.info("stopping server")
    }
}
