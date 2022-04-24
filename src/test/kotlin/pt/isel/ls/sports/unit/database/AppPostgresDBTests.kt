package pt.isel.ls.sports.unit.database

import org.junit.Before
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.JDBC_DATABASE_URL_ENV
import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.AppPostgresDB
import pt.isel.ls.sports.database.utils.runScript
import kotlin.test.BeforeTest

abstract class AppPostgresDBTests {
    companion object {
        private val jdbcDatabaseURL: String? = System.getenv(JDBC_DATABASE_URL_ENV)
        val dataSource = PGSimpleDataSource().apply { if (jdbcDatabaseURL != null) setURL(jdbcDatabaseURL) }
        lateinit var db: AppDB
    }

    @Before
    fun setupEnv() {
        val jdbcDatabaseURL: String? = System.getenv(JDBC_DATABASE_URL_ENV)
        org.junit.Assume.assumeNotNull(jdbcDatabaseURL)
    }

    @BeforeTest
    fun setupDatabase() {
        if (jdbcDatabaseURL != null)
            db = AppPostgresDB(jdbcDatabaseURL)

        db.reset()

        dataSource.connection.use {
            it.runScript("src/main/sql/addData.sql")
        }
    }
    // TODO: 06/04/2022 Add tests for db.execute transactions error handling
}
