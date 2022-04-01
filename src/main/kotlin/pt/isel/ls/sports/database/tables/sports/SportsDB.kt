package pt.isel.ls.sports.database.tables.sports

import pt.isel.ls.sports.domain.Sport

/**
 * Sports database representation.
 */
interface SportsDB {
    /**
     * Create a new sport.
     *
     * @param uid user's unique identifier
     * @param name the sport's name
     * @param description the sport's description
     *
     * @return the sport's unique identifier
     */
    fun createNewSport(uid: Int, name: String, description: String? = null): Int

    /**
     * Get a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return the sport object
     */
    fun getSport(sid: Int): Sport

    /**
     * Get the list of all sports.
     *
     * @return list of sport objects
     */
    fun getAllSports(): List<Sport>

    /**
     * Verifies if a sport exists with the given [sid]
     *
     * @param sid sport's unique identifier
     *
     * @return true if the sport exists, false otherwise
     */
    fun hasSport(sid: Int): Boolean
}
