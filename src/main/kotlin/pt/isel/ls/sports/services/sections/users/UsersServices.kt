package pt.isel.ls.sports.services.sections.users

import pt.isel.ls.sports.database.AlreadyExistsException
import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.sections.activities.ActivitiesResponse
import pt.isel.ls.sports.database.sections.users.UsersResponse
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.services.AbstractServices
import pt.isel.ls.sports.services.InvalidArgumentException
import java.util.UUID

class UsersServices(db: AppDB) : AbstractServices(db) {
    /**
     * Creates a new user in the database.
     *
     * @param name user's name
     * @param email user's email
     *
     * @return a createUser response (user's token and the user's unique identifier)
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
     * Gets the user identified by [uid].
     *
     * @param uid user's identifier
     *
     * @return user object
     */
    fun getUser(uid: Int): User {
        validateUid(uid)

        return db.execute { conn ->
            db.users.getUser(conn, uid)
        }
    }

    /**
     * Get the list of users.
     *
     * @return list of user objects
     */
    fun getAllUsers(skip: Int, limit: Int): UsersResponse = db.execute { conn ->
        db.users.getAllUsers(conn, skip, limit)
    }

    /**
     * Get all the activities made from a user.
     *
     * @param uid user's unique identifier
     *
     * @return list of activities made from a user
     */
    fun getUserActivities(uid: Int, skip: Int, limit: Int): ActivitiesResponse {
        validateUid(uid)

        return db.execute { conn ->
            db.activities.getUserActivities(conn, uid, skip, limit)
        }
    }
}
