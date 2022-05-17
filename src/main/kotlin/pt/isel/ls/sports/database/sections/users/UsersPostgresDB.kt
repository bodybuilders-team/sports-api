package pt.isel.ls.sports.database.sections.users

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.AlreadyExistsException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.database.utils.getPaginatedQuery
import pt.isel.ls.sports.domain.User
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

/**
 * Users database representation using Postgres.
 */
class UsersPostgresDB : UsersDB {

    override fun createNewUser(conn: ConnectionDB, name: String, email: String, hashedPassword: String): Int {
        if (hasUserWithEmail(conn, email))
            throw AlreadyExistsException("Email already in use")

        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                INSERT INTO users(name, email, password)
                VALUES (?, ?, ?)
                """.trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
        stm.setString(1, name)
        stm.setString(2, email)
        stm.setString(3, hashedPassword)

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
            throw NotFoundException("User with id $uid not found")
    }

    override fun getAllUsers(conn: ConnectionDB, skip: Int, limit: Int): UsersResponse {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                getPaginatedQuery(
                    """
                SELECT *
                FROM users
                    """.trimIndent()
                )
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
         * Gets a [UsersResponse] returned from the execution of the statement [stm].
         *
         * @param stm statement
         * @return [UsersResponse] with the list of users
         */
        fun getUsersResponse(stm: PreparedStatement): UsersResponse {
            val rs = stm.executeQuery()
            val users = mutableListOf<User>()

            rs.next()
            val totalCount = rs.getInt("totalCount")

            if (rs.getObject("id") != null)
                do {
                    users.add(getUserFromTable(rs))
                } while (rs.next())

            return UsersResponse(users, totalCount)
        }

        /**
         * Gets a [User] from a ResultSet.
         *
         * @param rs table
         * @return user object
         */
        fun getUserFromTable(rs: ResultSet) = User(
            id = rs.getInt(1),
            name = rs.getString(2),
            email = rs.getString(3),
            password = rs.getString(4)
        )

        /**
         * Executes a query on the "users" table given the [uid].
         *
         * @param conn connection
         * @param uid user's unique identifier
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
