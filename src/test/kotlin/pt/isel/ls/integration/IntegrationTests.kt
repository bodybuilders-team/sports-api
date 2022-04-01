package pt.isel.ls.integration

import org.http4k.client.JavaHttpClient
import org.junit.AfterClass
import org.junit.BeforeClass
import pt.isel.ls.runScript
import pt.isel.ls.sports.AppServer
import pt.isel.ls.sports.DEFAULT_PORT
import pt.isel.ls.sports.JDBC_DATABASE_URL_ENV
import pt.isel.ls.sports.PORT_ENV
import pt.isel.ls.sports.database.AppPostgresDB
import kotlin.test.BeforeTest

abstract class IntegrationTests {

    companion object {
        private val jdbcDatabaseURL: String = System.getenv(JDBC_DATABASE_URL_ENV)
        private val port = System.getenv(PORT_ENV)?.toIntOrNull() ?: DEFAULT_PORT

        val dataSource = AppPostgresDB.createPostgresDataSource(jdbcDatabaseURL)
        val db = AppPostgresDB(dataSource)
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
    fun setupDatabase() {
        dataSource.connection.use {
            it.runScript("src/main/sql/createSchema.sql")
        }
    }
}
