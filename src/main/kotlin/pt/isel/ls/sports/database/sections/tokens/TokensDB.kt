package pt.isel.ls.sports.database.sections.tokens

import pt.isel.ls.sports.database.connection.ConnectionDB
import java.util.UUID

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
     * @return uid
     */
    fun getUID(conn: ConnectionDB, token: String): Int
}
