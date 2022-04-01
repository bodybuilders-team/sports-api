package pt.isel.ls.sports.database

import pt.isel.ls.sports.database.tables.activities.ActivitiesDB
import pt.isel.ls.sports.database.tables.routes.RoutesDB
import pt.isel.ls.sports.database.tables.sports.SportsDB
import pt.isel.ls.sports.database.tables.tokens.TokensDB
import pt.isel.ls.sports.database.tables.users.UsersDB

/**
 * App database representation, an aggregate of all database sections.
 */
interface AppDB {
    val users: UsersDB
    val sports: SportsDB
    val routes: RoutesDB
    val activities: ActivitiesDB
    val tokens: TokensDB
}
