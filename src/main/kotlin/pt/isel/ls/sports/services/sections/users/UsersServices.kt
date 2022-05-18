package pt.isel.ls.sports.services.sections.users

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.exceptions.AlreadyExistsException
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.database.sections.activities.ActivitiesResponse
import pt.isel.ls.sports.database.sections.users.UsersResponse
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.services.AbstractServices
import java.util.UUID

/**
 * Users services. Implements methods regarding users.
 */
class UsersServices(db: AppDB) : AbstractServices(db) {

    /**
     * Creates a new user.
     *
     * @param name name of the user
     * @param email email of the user
     * @param password password of the user
     *
     * @return the user's unique identifier
     * @throws InvalidArgumentException if the name is invalid
     * @throws InvalidArgumentException if the email is invalid
     * @throws AlreadyExistsException if a user with that email already exists
     */
    fun createNewUser(name: String, email: String, password: String): Int {
        if (!User.isValidName(name))
            throw InvalidArgumentException(
                "Name must be between ${User.MIN_NAME_LENGTH} and ${User.MAX_NAME_LENGTH} characters"
            )

        if (!User.isValidEmail(email))
            throw InvalidArgumentException("Invalid email")

        return db.execute { conn ->
            if (db.users.hasUserWithEmail(conn, email))
                throw AlreadyExistsException("Email already in use")

            val hashedPassword = User.hashPassword(password + name + email)

            db.users.createNewUser(conn, name, email, hashedPassword)
        }
    }

    /**
     * Logs a user in, by providing a token in exchange for a valid email and password.
     *
     * @param email email of the user
     * @param password password of the user
     *
     * @return [LoginUserResponse] object containing the user's unique identifier and a token
     * @throws InvalidArgumentException if the email is invalid
     * @throws InvalidArgumentException if the password is invalid
     * @throws NotFoundException if no user with that email exists
     */
    fun loginUser(email: String, password: String): LoginUserResponse {

        return db.execute { conn ->
            val user = db.users.getAllUsers(conn, 0, 1000).users.firstOrNull { it.email == email }
                ?: throw NotFoundException("User with that email was not found")

            if (!User.checkPassword(password + user.name + user.email, user.password))
                throw InvalidArgumentException("Password does not match")

            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), user.id)
            LoginUserResponse(token, user.id)
        }
    }

    /**
     * Gets a specific user.
     *
     * @param id user's unique identifier
     *
     * @return the user object
     * @throws InvalidArgumentException if [id] is negative
     * @throws NotFoundException if there's no user with the [id]
     */
    fun getUser(id: Int): User {
        validateUid(id)

        return db.execute { conn ->
            db.users.getUser(conn, id)
        }
    }

    /**
     * Gets all users.
     *
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [UsersResponse] with the list of users
     * @throws InvalidArgumentException if [skip] is invalid
     * @throws InvalidArgumentException if [limit] is invalid
     */
    fun getAllUsers(skip: Int, limit: Int): UsersResponse = db.execute { conn ->
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        db.users.getAllUsers(conn, skip, limit)
    }

    /**
     * Gets all the activities made by a specific user.
     *
     * @param id user's unique identifier
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [ActivitiesResponse] with the list of activities
     * @throws InvalidArgumentException if [id] is negative
     * @throws InvalidArgumentException if [skip] is invalid
     * @throws InvalidArgumentException if [limit] is invalid
     */
    fun getUserActivities(id: Int, skip: Int, limit: Int): ActivitiesResponse {
        validateUid(id)
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        return db.execute { conn ->
            db.activities.getUserActivities(conn, id, skip, limit)
        }
    }

    companion object {
        private val LIMIT_RANGE = 0..100
    }
}
