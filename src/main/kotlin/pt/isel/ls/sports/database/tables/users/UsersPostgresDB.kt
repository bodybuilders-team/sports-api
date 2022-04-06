package pt.isel.ls.sports.database.tables.users

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.database.postgres.AbstractPostgresDB
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class UsersPostgresDB(dataSource: PGSimpleDataSource) : AbstractPostgresDB(dataSource), UsersDB {

    override fun createNewUser(name: String, email: String): Int =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                INSERT INTO users(name, email)
                VALUES (?, ?)
                """.trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
            stm.setString(1, name)
            stm.setString(2, email)

            if (stm.executeUpdate() == 0)
                throw SQLException("Creating user failed, no rows affected.")

            val generatedKeys = stm.generatedKeys
            return if (generatedKeys.next()) generatedKeys.getInt(1) else -1
        }

    override fun getUser(uid: Int): User =
        useConnection { conn ->
            val rs = doUserQuery(conn, uid)

            if (rs.next())
                return getUserFromTable(rs)
            else
                throw AppError.NotFound("User with id $uid not found")
        }

    override fun getAllUsers(): List<User> =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM users
                """.trimIndent()
            )

            val rs = stm.executeQuery()
            val users = mutableListOf<User>()

            while (rs.next())
                users.add(getUserFromTable(rs))

            return users
        }

    override fun hasUserWithEmail(email: String): Boolean =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM users
                WHERE email = ?
                """.trimIndent()
            )
            stm.setString(1, email)

            val rs = stm.executeQuery()

            return rs.next()
        }

    override fun hasUser(uid: Int): Boolean =
        useConnection { conn ->
            val rs = doUserQuery(conn, uid)

            return rs.next()
        }

    companion object {
        /**
         * Gets a User object from a ResultSet.
         * @param rs table
         * @return user
         */
        private fun getUserFromTable(rs: ResultSet) = User(
            id = rs.getInt(1),
            name = rs.getString(2),
            email = rs.getString(3)
        )

        /**
         * Executes a query on the users table given the [uid].
         *
         * @param conn connection
         * @param uid user id
         * @return result set
         */
        private fun doUserQuery(conn: Connection, uid: Int): ResultSet {
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM users
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, uid)

            return stm.executeQuery()
        }
    }
}
