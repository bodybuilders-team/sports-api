package pt.isel.ls.sports.database.sections.users

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.connection.getPostgresConnection
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import java.sql.Connection
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
            throw AppError.NotFound("User with id $uid not found")
    }

    override fun getAllUsers(conn: ConnectionDB): List<User> {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
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