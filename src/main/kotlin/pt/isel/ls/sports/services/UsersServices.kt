package pt.isel.ls.sports.services

import pt.isel.ls.sports.api.routers.users.CreateUserResponse
import pt.isel.ls.sports.data.AppDatabase
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import java.util.*

class UsersServices(db: AppDatabase) : Services(db) {
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
            throw AppError.invalidArgument("Name must be between ${User.MIN_NAME_LENGTH} and ${User.MAX_NAME_LENGTH} characters")

        if (!User.isValidEmail(email))
            throw AppError.invalidArgument("Invalid email")

        if (db.users.hasUserWithEmail(email))
            throw AppError.invalidArgument("Email already in use")

        val uid = db.users.createNewUser(name, email)
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)

        return CreateUserResponse(token, uid)
    }

    /**
     * Gets the user identified by [uid].
     *
     * @param uid user's identifier
     *
     * @return user object
     */
    fun getUser(uid: Int): User {
        if (!isValidId(uid))
            throw AppError.invalidArgument("User id must be positive")

        return db.users.getUser(uid)
    }

    /**
     * Get the list of users.
     *
     * @return list of user objects
     */
    fun getAllUsers(): List<User> {
        return db.users.getAllUsers()
    }

    /**
     * Get all the activities made from a user.
     *
     * @param uid user's unique identifier
     *
     * @return list of activities made from a user
     */
    fun getUserActivities(uid: Int): List<Activity> {
        if (!isValidId(uid))
            throw AppError.invalidArgument("User id must be positive")

        return db.users.getUserActivities(uid)
    }
}
