package pt.isel.ls.sports.database

import pt.isel.ls.sports.database.memory.AppMemoryDBSource
import pt.isel.ls.sports.database.tables.activities.ActivitiesMemoryDB
import pt.isel.ls.sports.database.tables.routes.RoutesMemoryDB
import pt.isel.ls.sports.database.tables.sports.SportsMemoryDB
import pt.isel.ls.sports.database.tables.tokens.TokensMemoryDB
import pt.isel.ls.sports.database.tables.users.UsersMemoryDB

/**
 * Implementation of a memory database representation, an aggregate of all memory database sections.
 */
class AppMemoryDB(source: AppMemoryDBSource) : AppDB {
    override val users = UsersMemoryDB(source)
    override val sports = SportsMemoryDB(source)
    override val routes = RoutesMemoryDB(source)
    override val activities = ActivitiesMemoryDB(source)
    override val tokens = TokensMemoryDB(source)
}
