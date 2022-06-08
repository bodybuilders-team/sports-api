package pt.isel.ls.sports.unit.database

import org.junit.Before
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.DATABASE_URL_ENV
import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.AppPostgresDB
import pt.isel.ls.sports.database.utils.runScript
import kotlin.test.BeforeTest

abstract class AppPostgresDBTests {
    companion object {
        private val databaseURL: String? = System.getenv(DATABASE_URL_ENV)
        val dataSource = PGSimpleDataSource().apply {
            if (databaseURL != null)
                setURL("jdbc:$databaseURL?user=postgres&password=postgres")
        }
        lateinit var db: AppDB
    }

    @Before
    fun setupEnv() {
        val jdbcDatabaseURL: String? = System.getenv(DATABASE_URL_ENV)
        org.junit.Assume.assumeNotNull(jdbcDatabaseURL)
    }

    @BeforeTest
    fun setupDatabase() {
        if (databaseURL != null)
            db = AppPostgresDB("jdbc:$databaseURL?user=postgres&password=postgres")

        db.reset()

        dataSource.connection.use {
            it.runScript("src/main/sql/addData.sql")
        }
    }
}
