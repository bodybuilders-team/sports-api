package pt.isel.ls.sports.database.sections.sports

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.domain.Sport

/**
 * Sports database representation.
 */
interface SportsDB {
    /**
     * Create a new sport.
     *
     * @param conn database Connection
     * @param uid user's unique identifier
     * @param name the sport's name
     * @param description the sport's description
     *
     * @return the sport's unique identifier
     */
    fun createNewSport(conn: ConnectionDB, uid: Int, name: String, description: String? = null): Int

    /**
     * Get a sport.
     *
     * @param conn database Connection
     * @param sid sport's unique identifier
     *
     * @return the sport object
     */
    fun getSport(conn: ConnectionDB, sid: Int): Sport

    /**
     * Get the list of all sports.
     *
     * @param conn database Connection
     *
     * @return list of sport objects
     */
    fun getAllSports(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): SportsResponse

    /**
     * Verifies if a sport exists with the given [sid]
     *
     * @param conn database Connection
     * @param sid sport's unique identifier
     *
     * @return true if the sport exists, false otherwise
     */
    fun hasSport(conn: ConnectionDB, sid: Int): Boolean
}
