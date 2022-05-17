package pt.isel.ls.sports.database.sections.tokens

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.NotFoundException
import java.sql.SQLException
import java.util.UUID

/**
 * Tokens database representation using Postgres.
 */
class TokensPostgresDB : TokensDB {

    override fun createUserToken(conn: ConnectionDB, token: UUID, uid: Int): String {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                INSERT INTO tokens(token, uid)
                VALUES (?, ?)
                """.trimIndent()
            )
        val stringToken = token.toString()

        stm.setString(1, stringToken)
        stm.setInt(2, uid)

        if (stm.executeUpdate() == 0)
            throw SQLException("Creating token failed, no rows affected.")

        return stringToken
    }

    override fun getUID(conn: ConnectionDB, token: String): Int {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                SELECT uid
                FROM tokens
                WHERE token = ?
                """.trimIndent()
            )
        stm.setString(1, token)

        val rs = stm.executeQuery()

        if (rs.next())
            return rs.getInt(1)
        else
            throw NotFoundException("Token $token isn't associated to any user")
    }

    override fun hasUID(conn: ConnectionDB, token: String): Boolean {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                SELECT uid
                FROM tokens
                WHERE token = ?
                """.trimIndent()
            )
        stm.setString(1, token)

        val rs = stm.executeQuery()

        return rs.next()
    }
}
