package pt.isel.ls.sports.data.tokens

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.data.Postgres
import pt.isel.ls.sports.errors.AppError
import java.sql.SQLException
import java.util.*

class TokensPostgres(dataSource: PGSimpleDataSource) : Postgres(dataSource), TokensDatabase {

    override fun createUserToken(token: UUID, uid: Int): String =
        useConnection { conn ->
            val stm = conn.prepareStatement(
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

    override fun getUID(token: String): Int =
        useConnection { conn ->
            val stm = conn.prepareStatement(
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
                throw AppError.notFound("Token $token isn't associated to any user")
        }
}
