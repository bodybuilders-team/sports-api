package pt.isel.ls.sports.database

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.connection.PostgresConnectionDB
import pt.isel.ls.sports.database.exceptions.DatabaseAccessException
import pt.isel.ls.sports.database.exceptions.DatabaseRollbackException
import pt.isel.ls.sports.database.sections.activities.ActivitiesPostgresDB
import pt.isel.ls.sports.database.sections.routes.RoutesPostgresDB
import pt.isel.ls.sports.database.sections.sports.SportsPostgresDB
import pt.isel.ls.sports.database.sections.tokens.TokensPostgresDB
import pt.isel.ls.sports.database.sections.users.UsersPostgresDB
import pt.isel.ls.sports.database.utils.rollbackTransaction
import pt.isel.ls.sports.database.utils.runScript
import pt.isel.ls.sports.utils.Logger
import java.sql.SQLException

/**
 * App database representation using Postgres, an aggregate of all Postgres database sections.
 *
 * @param sourceURL the Postgres database URL
 * @property source the Postgres database source obtained from the URL
 */
class AppPostgresDB(sourceURL: String) : AppDB {
    private val source = PGSimpleDataSource().apply { setURL(sourceURL) }

    /**
     * Encapsulates a function that interacts with the database in order to allow for atomic sets of operations.
     *
     * @param func function that interacts with the database
     *
     * @return result of [func]
     * @throws DatabaseAccessException if the database cannot be accessed.
     * @throws DatabaseRollbackException if the database cannot be rolled back.
     */
    override fun <R> execute(func: (ConnectionDB) -> R): R {
        val conn = try {
            source.connection
        } catch (e: SQLException) {
            throw DatabaseAccessException()
        }

        conn.autoCommit = false

        return try {
            func(PostgresConnectionDB(conn)).also { conn.commit() }
        } catch (e: Exception) {
            rollbackTransaction(conn)

            Logger.warn("Transaction rollback successful")
            throw e
        } finally {
            conn.close()
        }
    }

    override fun reset() {
        source.connection.use {
            it.runScript("src/main/sql/cleanData.sql")
        }
    }

    override val users: UsersPostgresDB = UsersPostgresDB()
    override val sports: SportsPostgresDB = SportsPostgresDB()
    override val routes: RoutesPostgresDB = RoutesPostgresDB()
    override val activities: ActivitiesPostgresDB = ActivitiesPostgresDB()
    override val tokens = TokensPostgresDB()
}
