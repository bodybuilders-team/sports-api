package pt.isel.ls.sports.database.sections.users

import pt.isel.ls.sports.database.AlreadyExistsException
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.domain.User

/**
 * Users database representation.
 */
interface UsersDB {
    /**
     * Creates a new user.
     *
     * @param conn database Connection
     * @param name name of the user
     * @param email email of the user
     *
     * @return user's unique identifier
     * @throws AlreadyExistsException if a user with the [email] already exists
     */
    fun createNewUser(conn: ConnectionDB, name: String, email: String): Int

    /**
     * Gets a specific user.
     *
     * @param conn database Connection
     * @param uid user's identifier
     *
     * @return user object
     * @throws NotFoundException if there's no user with the [uid]
     */
    fun getUser(conn: ConnectionDB, uid: Int): User

    /**
     * Gets all users.
     *
     * @param conn database Connection
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [UsersResponse] with a list of users
     */
    fun getAllUsers(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): UsersResponse

    /**
     * Verifies if a user with the given [email] exists.
     *
     * @param conn database Connection
     * @param email email of the user
     *
     * @return true if the user exists, false otherwise
     */
    fun hasUserWithEmail(conn: ConnectionDB, email: String): Boolean

    /**
     * Verifies if a user with the given [uid] exists.
     *
     * @param conn database Connection
     * @param uid user's unique identifier
     *
     * @return true if the user exists, false otherwise
     */
    fun hasUser(conn: ConnectionDB, uid: Int): Boolean
}
