package pt.isel.ls.sports.database

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.connection.MemoryConnectionDB
import pt.isel.ls.sports.database.sections.activities.ActivitiesMemoryDB
import pt.isel.ls.sports.database.sections.routes.RoutesMemoryDB
import pt.isel.ls.sports.database.sections.sports.SportsMemoryDB
import pt.isel.ls.sports.database.sections.tokens.TokensMemoryDB
import pt.isel.ls.sports.database.sections.users.UsersMemoryDB

/**
 * App database representation using memory, an aggregate of all memory database sections.
 *
 * @property source the memory source of the database
 */
class AppMemoryDB(private val source: AppMemoryDBSource) : AppDB {
    override fun <R> execute(func: (ConnectionDB) -> R): R =
        func(MemoryConnectionDB())

    override fun reset() = source.reset()

    override val users = UsersMemoryDB(source)
    override val sports = SportsMemoryDB(source)
    override val routes = RoutesMemoryDB(source)
    override val activities = ActivitiesMemoryDB(source)
    override val tokens = TokensMemoryDB(source)
}
