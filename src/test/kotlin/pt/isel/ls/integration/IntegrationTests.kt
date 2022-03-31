package pt.isel.ls.integration

import org.http4k.client.JavaHttpClient
import org.junit.AfterClass
import org.junit.BeforeClass
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.runScript
import pt.isel.ls.sports.SportsServer
import pt.isel.ls.sports.data.SportsPostgres
import kotlin.test.BeforeTest

abstract class IntegrationTests {

    companion object {
        private val jdbcDatabaseURL: String = System.getenv("JDBC_DATABASE_URL")
        private val port = System.getenv("PORT")?.toIntOrNull() ?: 8888

        val dataSource = PGSimpleDataSource().apply {
            this.setURL(jdbcDatabaseURL)
        }
        val db = SportsPostgres(jdbcDatabaseURL)
        val send = JavaHttpClient()
        val uriPrefix = "http://localhost:$port/api"

        private val server = SportsServer(port, db)

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
