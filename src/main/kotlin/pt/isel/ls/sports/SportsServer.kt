package pt.isel.ls.sports

import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.sports.api.SportsWebApi
import pt.isel.ls.sports.data.SportsDatabase
import pt.isel.ls.sports.services.SportsServices

/**
 * Represents a Sports API application server.
 * @property port port where the server is running
 * @property dataSource application data source
 * @property server HTTP4K server
 */
class SportsServer(private val port: Int, private val dataSource: SportsDatabase) {

    private val server: Http4kServer

    init {
        val services = SportsServices(dataSource)
        val webApi = SportsWebApi(services)
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
