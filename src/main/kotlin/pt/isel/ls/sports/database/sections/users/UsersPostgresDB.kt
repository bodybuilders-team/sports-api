package pt.isel.ls.sports.database.sections.users

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.connection.getPostgresConnection
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppException
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class UsersPostgresDB : UsersDB {

    override fun createNewUser(conn: ConnectionDB, name: String, email: String): Int {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
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

    override fun getUser(conn: ConnectionDB, uid: Int): User {
        val pgConn = conn.getPostgresConnection()

        val rs = doUserQuery(pgConn, uid)

        if (rs.next())
            return getUserFromTable(rs)
        else
            throw AppException.NotFound("User with id $uid not found")
    }

    override fun getAllUsers(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): UsersResponse {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                SELECT *, count(*) OVER() AS totalCount
                FROM users
                OFFSET ?
                LIMIT ?
                """.trimIndent()
            )

        stm.setInt(1, skip)
        stm.setInt(2, limit)

        return getUsersResponse(stm)
    }

    override fun hasUserWithEmail(conn: ConnectionDB, email: String): Boolean {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
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

    override fun hasUser(conn: ConnectionDB, uid: Int): Boolean {
        val pgConn = conn.getPostgresConnection()
        val rs = doUserQuery(pgConn, uid)

        return rs.next()
    }

    companion object {
        /**
         * Gets a list of users returned from the execution of the statement [stm]
         *
         * @param stm statement
         *
         * @return list of users
         */
        fun getUsersResponse(stm: PreparedStatement): UsersResponse {
            val rs = stm.executeQuery()
            val users = mutableListOf<User>()
            var total = 0

            while (rs.next()) {
                total = rs.getInt("totalCount")
                users.add(getUserFromTable(rs))
            }
            return UsersResponse(users, total)
        }

        /**
         * Gets a User object from a ResultSet.
         * @param rs table
         * @return user
         */
        fun getUserFromTable(rs: ResultSet) = User(
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
