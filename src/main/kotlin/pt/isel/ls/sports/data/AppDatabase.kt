package pt.isel.ls.sports.data

import pt.isel.ls.sports.data.activities.ActivitiesDatabase
import pt.isel.ls.sports.data.routes.RoutesDatabase
import pt.isel.ls.sports.data.sports.SportsDatabase
import pt.isel.ls.sports.data.tokens.TokensDatabase
import pt.isel.ls.sports.data.users.UsersDatabase

/**
 * App database representation, an aggregate of all databases.
 */
interface AppDatabase {
    val users: UsersDatabase
    val sports: SportsDatabase
    val routes: RoutesDatabase
    val tokens: TokensDatabase
    val activities: ActivitiesDatabase
}
