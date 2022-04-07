package pt.isel.ls.sports.database.sections.tokens

import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.errors.AppError
import java.util.UUID

class TokensMemoryDB(private val source: AppMemoryDBSource) : TokensDB {
    override fun createUserToken(conn: ConnectionDB, token: UUID, uid: Int): String {
        val stringToken = token.toString()
        source.tokens[stringToken] = uid
        return stringToken
    }

    override fun getUID(conn: ConnectionDB, token: String): Int {
        return source.tokens[token] ?: throw AppError.NotFound("Token $token isn't associated to any user")
    }
}
