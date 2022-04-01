package pt.isel.ls.sports.database.sections.tokens

import java.util.UUID

interface TokensDB {
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
