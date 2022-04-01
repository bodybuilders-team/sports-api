package pt.isel.ls.sports.services

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.errors.AppError

abstract class AbstractServices(protected val db: AppDB) {

    /**
     * Gets the user's unique identifier associate with the [token]
     *
     * @param token user token
     *
     * @return user's unique identifier associated with the [token]
     *
     * @throws AppError.invalidCredentials if a user with the [token] was not found
     */
    protected fun authenticate(token: String) = runCatching {
        db.tokens.getUID(token)
    }.getOrElse {
        when (it) {
            is AppError -> {
                if (it == AppError.notFound()) throw AppError.invalidCredentials()
                else throw it
            }
            else -> throw it
        }
    }
}
