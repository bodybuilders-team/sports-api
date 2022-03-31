package pt.isel.ls.sports.services

import pt.isel.ls.sports.data.AppDatabase
import pt.isel.ls.sports.errors.AppError

abstract class Services(val db: AppDatabase) {

    /**
     * Gets the user's unique identifier associate with the [token]
     *
     * @param token user token
     *
     * @return user's unique identifier associated with the [token]
     *
     * @throws AppError.invalidCredentials if a user with the [token] was not found
     */
    fun authenticate(token: String) = runCatching {
        db.tokens.getUID(token)
    }.getOrElse { throw AppError.invalidCredentials() }
}
