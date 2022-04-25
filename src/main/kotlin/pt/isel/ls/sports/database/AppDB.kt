package pt.isel.ls.sports.database

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.sections.activities.ActivitiesDB
import pt.isel.ls.sports.database.sections.routes.RoutesDB
import pt.isel.ls.sports.database.sections.sports.SportsDB
import pt.isel.ls.sports.database.sections.tokens.TokensDB
import pt.isel.ls.sports.database.sections.users.UsersDB

/**
 * App database representation, an aggregate of all database sections.
 */
interface AppDB {
    /**
     * Encapsulates a function that interacts with the database in order to allow for atomic sets of operations.
     *
     * @param func function that interacts with the database
     * @return result of [func]
     */
    fun <R> execute(func: (ConnectionDB) -> R): R

    /**
     * Resets database.
     */
    fun reset()

    val users: UsersDB
    val sports: SportsDB
    val routes: RoutesDB
    val activities: ActivitiesDB
    val tokens: TokensDB
}
