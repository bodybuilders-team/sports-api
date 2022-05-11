package pt.isel.ls.sports.services.sections.users

import pt.isel.ls.sports.database.AlreadyExistsException
import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.InvalidArgumentException
import pt.isel.ls.sports.database.NotFoundException
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
     *
     * @return [CreateUserResponse] with the user's token and the user's unique identifier
     * @throws InvalidArgumentException if the name is invalid
     * @throws InvalidArgumentException if the email is invalid
     * @throws AlreadyExistsException if a user with that email already exists
     */
    fun createNewUser(name: String, email: String): CreateUserResponse {
        if (!User.isValidName(name))
            throw InvalidArgumentException("Name must be between ${User.MIN_NAME_LENGTH} and ${User.MAX_NAME_LENGTH} characters")

        if (!User.isValidEmail(email))
            throw InvalidArgumentException("Invalid email")

        return db.execute { conn ->
            if (db.users.hasUserWithEmail(conn, email))
                throw AlreadyExistsException("Email already in use")

            val uid = db.users.createNewUser(conn, name, email)
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            CreateUserResponse(token, uid)
        }
    }

    /**
     * Gets a specific user.
     *
     * @param uid user's unique identifier
     *
     * @return the user object
     * @throws InvalidArgumentException if [uid] is negative
     * @throws NotFoundException if there's no user with the [uid]
     */
    fun getUser(uid: Int): User {
        validateUid(uid)

        return db.execute { conn ->
            db.users.getUser(conn, uid)
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
     * @param uid user's unique identifier
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [ActivitiesResponse] with the list of activities
     * @throws InvalidArgumentException if [uid] is negative
     * @throws InvalidArgumentException if [skip] is invalid
     * @throws InvalidArgumentException if [limit] is invalid
     */
    fun getUserActivities(uid: Int, skip: Int, limit: Int): ActivitiesResponse {
        validateUid(uid)
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        return db.execute { conn ->
            db.activities.getUserActivities(conn, uid, skip, limit)
        }
    }

    companion object {
        private val LIMIT_RANGE = 0..100
    }
}
