package pt.isel.ls.sports.data.users

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.data.Postgres
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import java.sql.SQLException
import java.sql.Statement

class UsersPostgres(dataSource: PGSimpleDataSource) : Postgres(dataSource), UsersDatabase {
    /**
     * Creates a new user in the database.
     *
     * @param name user's name
     * @param email user's email
     *
     * @return user's unique identifier
     */
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

    /**
     * Gets the user object.
     *
     * @param uid user's identifier
     *
     * @return user object
     */
    override fun getUser(uid: Int): User =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM users
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, uid)

            val rs = stm.executeQuery()

            if (rs.next())
                return getUserFromTable(rs)
            else
                throw AppError.notFound("User with id $uid not found")
        }

    /**
     * Get the list of users.
     *
     * @return list of user identifiers
     */
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

    /**
     * Get all the activities made from a user.
     *
     * @param uid user's unique identifier
     *
     * @return list of identifiers of activities made from a user
     */
    override fun getUserActivities(uid: Int): List<Activity> =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM activities
                WHERE uid = ?
                """.trimIndent()
            )
            stm.setInt(1, uid)

            return getActivities(stm)
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
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM users
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, uid)

            val rs = stm.executeQuery()

            return rs.next()
        }
}
