package pt.isel.ls.sports.integration

import org.http4k.client.JavaHttpClient
import org.junit.AfterClass
import org.junit.BeforeClass
import pt.isel.ls.sports.AppServer
import pt.isel.ls.sports.DATABASE_URL_ENV
import pt.isel.ls.sports.DEFAULT_PORT
import pt.isel.ls.sports.PORT_ENV
import pt.isel.ls.sports.database.AppMemoryDB
import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.AppPostgresDB
import kotlin.test.BeforeTest

abstract class IntegrationTests {

    companion object {
        private val port = System.getenv(PORT_ENV)?.toIntOrNull() ?: DEFAULT_PORT
        private val databaseURL: String? = System.getenv(DATABASE_URL_ENV)

        val db = if (databaseURL != null)
            AppPostgresDB("jdbc:$databaseURL?user=postgres&password=postgres")
        else
            AppMemoryDB(AppMemoryDBSource())

        val send = JavaHttpClient()
        val uriPrefix = "http://localhost:$port/api"

        private val server = AppServer(port, db)

        @BeforeClass
        @JvmStatic
        fun start() {
            server.start()
        }

        @AfterClass
        @JvmStatic
        fun stop() {
            server.stop()
        }
    }

    @BeforeTest
    fun initializeDataMem() {
        db.reset()
    }
}
