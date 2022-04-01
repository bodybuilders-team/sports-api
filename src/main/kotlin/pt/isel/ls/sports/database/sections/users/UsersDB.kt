package pt.isel.ls.sports.database.sections.users

import pt.isel.ls.sports.domain.User

/**
 * Users database representation.
 */
interface UsersDB {
    /**
     * Creates a new user in the database.
     *
     * @param name user's name
     * @param email user's email
     *
     * @return user's unique identifier
     */
    fun createNewUser(name: String, email: String): Int

    /**
     * Gets the user identified by [uid].
     *
     * @param uid user's identifier
     *
     * @return user object
     */
    fun getUser(uid: Int): User

    /**
     * Get the list of users.
     *
     * @return list of user objects
     */
    fun getAllUsers(): List<User>

    /**
     * Verifies if a user exists with the given [email]
     *
     * @param email user's email
     *
     * @return true if the user exists, false otherwise
     */
    fun hasUserWithEmail(email: String): Boolean

    /**
     * Verifies if a user exists with the given [uid]
     *
     * @param uid user's unique identifier
     *
     * @return true if the user exists, false otherwise
     */
    fun hasUser(uid: Int): Boolean
}
