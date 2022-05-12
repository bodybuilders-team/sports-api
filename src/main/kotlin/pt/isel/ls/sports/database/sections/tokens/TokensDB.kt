package pt.isel.ls.sports.database.sections.tokens

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.NotFoundException
import java.util.UUID

/**
 * Tokens database representation.
 */
interface TokensDB {

    /**
     * Associates a user [token] with the [uid].
     *
     * @param conn database Connection
     * @param token user token
     * @param uid user's identifier
     *
     * @return user's token
     */
    fun createUserToken(conn: ConnectionDB, token: UUID, uid: Int): String

    /**
     * Gets the uid associated with the [token].
     *
     * @param conn database Connection
     * @param token user's token
     *
     * @return user's unique identifier
     * @throws NotFoundException if the token isn't associated with any user
     */
    fun getUID(conn: ConnectionDB, token: String): Int
}
