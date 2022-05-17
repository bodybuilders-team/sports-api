package pt.isel.ls.sports.database.sections.tokens

import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.NotFoundException
import java.util.UUID

/**
 * Tokens database representation using memory.
 */
class TokensMemoryDB(private val source: AppMemoryDBSource) : TokensDB {

    override fun createUserToken(conn: ConnectionDB, token: UUID, uid: Int): String {
        val stringToken = token.toString()
        source.tokens[stringToken] = uid
        return stringToken
    }

    override fun getUID(conn: ConnectionDB, token: String): Int =
        source.tokens[token] ?: throw NotFoundException("Token $token isn't associated to any user")

    override fun hasUID(conn: ConnectionDB, token: String): Boolean =
        source.tokens.containsKey(token)
}
