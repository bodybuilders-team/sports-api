package pt.isel.ls.sports.database.sections.tokens

import pt.isel.ls.sports.database.memory.AppMemoryDBSource
import pt.isel.ls.sports.errors.AppError
import java.util.UUID

class TokensMemoryDB(private val source: AppMemoryDBSource) : TokensDB {
    override fun createUserToken(token: UUID, uid: Int): String {
        val stringToken = token.toString()
        source.tokens[stringToken] = uid
        return stringToken
    }

    override fun getUID(token: String): Int {
        return source.tokens[token] ?: throw AppError.notFound("Token $token isn't associated to any user")
    }
}
