package pt.isel.ls.unit.postgres

import org.junit.Before
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.JDBC_DATABASE_URL_ENV
import pt.isel.ls.sports.database.AppPostgresDB
import pt.isel.ls.sports.database.utils.runScript
import kotlin.test.BeforeTest

abstract class AppPostgresDBTests {
    companion object {
        private val jdbcDatabaseURL: String = System.getenv(JDBC_DATABASE_URL_ENV)
        val dataSource = PGSimpleDataSource().apply { setURL(jdbcDatabaseURL) }
        val db = AppPostgresDB(jdbcDatabaseURL)
    }

    @Before
    fun setupEnv() {
        org.junit.Assume.assumeTrue(System.getenv("TEST_POSTGRES")?.toBoolean() ?: false)
    }

    @BeforeTest
    fun setupDatabase() {
        db.reset()

        dataSource.connection.use {
            it.runScript("src/main/sql/addData.sql")
        }
    }

// TODO: 26/03/2022 Add more tests (synchronize with AppMemoryTests?)
// TODO: 06/04/2022 Add tests for db.execute transactions error handling
}
