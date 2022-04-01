package pt.isel.ls.sports.database

import pt.isel.ls.sports.database.sections.activities.ActivitiesDB
import pt.isel.ls.sports.database.sections.routes.RoutesDB
import pt.isel.ls.sports.database.sections.sports.SportsDB
import pt.isel.ls.sports.database.sections.tokens.TokensDB
import pt.isel.ls.sports.database.sections.users.UsersDB

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
