package pt.isel.ls.sports.data.tokens

import java.util.*

/**
 * Tokens database representation.
 */
interface TokensDatabase {
    /**
     * Associates a user [token] with the [uid].
     *
     * @param token user token
     * @param uid user's identifier
     *
     * @return user's token
     */
    fun createUserToken(token: UUID, uid: Int): String

    /**
     * Gets the uid associated with the [token].
     *
     * @param token user's token
     *
     * @return uid
     */
    fun getUID(token: String): Int
}
