package pt.isel.ls.sports.database.sections.sports

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.domain.Sport

/**
 * Sports database representation.
 */
interface SportsDB {
    /**
     * Creates a new sport.
     *
     * @param conn database Connection
     * @param uid user's unique identifier
     * @param name name of the sport
     * @param description description of the sport
     *
     * @return sport's unique identifier
     * @throws NotFoundException if there's no user with the [uid]
     */
    fun createNewSport(conn: ConnectionDB, uid: Int, name: String, description: String? = null): Int

    /**
     * Gets a specific sport.
     *
     * @param conn database Connection
     * @param sid sport's unique identifier
     *
     * @return the sport object
     * @throws NotFoundException if there's no sport with the [sid]
     */
    fun getSport(conn: ConnectionDB, sid: Int): Sport

    /**
     * Gets all sports.
     *
     * @param conn database Connection
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [SportsResponse] with a list of sports
     */
    fun getAllSports(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): SportsResponse

    /**
     * Verifies if a sport with the given [sid] exists.
     *
     * @param conn database Connection
     * @param sid sport's unique identifier
     *
     * @return true if the sport exists, false otherwise
     */
    fun hasSport(conn: ConnectionDB, sid: Int): Boolean
}
