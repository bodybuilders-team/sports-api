package pt.isel.ls.sports.services.sections

import pt.isel.ls.sports.api.routers.users.dtos.CreateUserResponse
import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.services.AbstractServices
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
            throw AppException.InvalidArgument("Name must be between ${User.MIN_NAME_LENGTH} and ${User.MAX_NAME_LENGTH} characters")

        if (!User.isValidEmail(email))
            throw AppException.InvalidArgument("Invalid email")

        return db.execute { conn ->
            if (db.users.hasUserWithEmail(conn, email))
                throw AppException.Conflict("Email already in use")

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
    fun getAllUsers(): List<User> = db.execute { conn ->
        db.users.getAllUsers(conn)
    }

    /**
     * Get all the activities made from a user.
     *
     * @param uid user's unique identifier
     *
     * @return list of activities made from a user
     */
    fun getUserActivities(uid: Int): List<Activity> {
        validateUid(uid)

        return db.execute { conn ->
            db.activities.getUserActivities(conn, uid)
        }
    }
}
