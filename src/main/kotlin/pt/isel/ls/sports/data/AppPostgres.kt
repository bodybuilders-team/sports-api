package pt.isel.ls.sports.data

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.data.activities.ActivitiesPostgres
import pt.isel.ls.sports.data.routes.RoutesPostgres
import pt.isel.ls.sports.data.sports.SportsPostgres
import pt.isel.ls.sports.data.tokens.TokensPostgres
import pt.isel.ls.sports.data.users.UsersPostgres

class AppPostgres(source: PGSimpleDataSource) : AppDatabase {
    override val users: UsersPostgres = UsersPostgres(source)
    override val sports: SportsPostgres = SportsPostgres(source)
    override val routes: RoutesPostgres = RoutesPostgres(source)
    override val tokens: TokensPostgres = TokensPostgres(source)
    override val activities: ActivitiesPostgres = ActivitiesPostgres(source)

    companion object {
        fun createPostgresDataSource(jdbcDatabaseURL: String) = PGSimpleDataSource().apply {
            setURL(jdbcDatabaseURL)
        }
    }
}
