package pt.isel.ls.sports.database.sections.users

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.domain.User

/**
 * Users database representation.
 */
interface UsersDB {
    /**
     * Creates a new user in the database.
     *
     * @param conn database Connection
     * @param name user's name
     * @param email user's email
     *
     * @return user's unique identifier
     */
    fun createNewUser(conn: ConnectionDB, name: String, email: String): Int

    /**
     * Gets the user identified by [uid].
     *
     * @param conn database Connection
     * @param uid user's identifier
     *
     * @return user object
     */
    fun getUser(conn: ConnectionDB, uid: Int): User

    /**
     * Get the list of users.
     *
     * @param conn database Connection
     * @return list of user objects
     */
    fun getAllUsers(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): UsersResponse

    /**
     * Verifies if a user exists with the given [email]
     *
     * @param conn database Connection
     * @param email user's email
     *
     * @return true if the user exists, false otherwise
     */
    fun hasUserWithEmail(conn: ConnectionDB, email: String): Boolean

    /**
     * Verifies if a user exists with the given [uid]
     *
     * @param conn database Connection
     * @param uid user's unique identifier
     *
     * @return true if the user exists, false otherwise
     */
    fun hasUser(conn: ConnectionDB, uid: Int): Boolean
}
