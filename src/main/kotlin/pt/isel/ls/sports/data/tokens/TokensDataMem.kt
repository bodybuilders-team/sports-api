package pt.isel.ls.sports.data.tokens

import pt.isel.ls.sports.data.AppDataMemSource
import pt.isel.ls.sports.errors.AppError
import java.util.*

class TokensDataMem(private val source: AppDataMemSource) : TokensDatabase {

    override fun createUserToken(token: UUID, uid: Int): String {
        val stringToken = token.toString()
        source.tokens[stringToken] = uid
        return stringToken
    }

    override fun getUID(token: String): Int {
        return source.tokens[token] ?: throw AppError.notFound("Token $token isn't associated to any user")
    }
}
