package pt.isel.ls.sports.database

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.database.tables.activities.ActivitiesPostgresDB
import pt.isel.ls.sports.database.tables.routes.RoutesPostgresDB
import pt.isel.ls.sports.database.tables.sports.SportsPostgresDB
import pt.isel.ls.sports.database.tables.tokens.TokensPostgresDB
import pt.isel.ls.sports.database.tables.users.UsersPostgresDB

/**
 * Implementation of a Postgres database representation, an aggregate of all Postgres database sections.
 */
class AppPostgresDB(source: PGSimpleDataSource) : AppDB {
    override val users: UsersPostgresDB = UsersPostgresDB(source)
    override val sports: SportsPostgresDB = SportsPostgresDB(source)
    override val routes: RoutesPostgresDB = RoutesPostgresDB(source)
    override val activities: ActivitiesPostgresDB = ActivitiesPostgresDB(source)
    override val tokens = TokensPostgresDB(source)

    companion object {
        fun createPostgresDataSource(jdbcDatabaseURL: String) = PGSimpleDataSource().apply {
            setURL(jdbcDatabaseURL)
        }
    }
}
