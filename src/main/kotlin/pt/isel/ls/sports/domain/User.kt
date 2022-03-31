package pt.isel.ls.sports.domain

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.services.isValidId

/**
 * User representation.
 *
 * @property id user's unique identifier
 * @property name name of the user
 * @property email email of the user
 */
@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String
) {
    companion object {
        const val MIN_NAME_LENGTH = 3
        const val MAX_NAME_LENGTH = 60

        private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"

        /**
         * Checks if a email e valid.
         * @param email email to check
         * @return true if its valid
         */
        fun isValidEmail(email: String) =
            email.matches(Regex(EMAIL_REGEX))

        /**
         * Checks if a name e valid.
         * @param name name to check
         * @return true if its valid
         */
        fun isValidName(name: String) =
            name.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH
    }

    init {
        require(isValidId(id)) { "Invalid user id: $id" }
        require(isValidName(name)) { "Invalid user name: $name" }
        require(isValidEmail(email)) { "Invalid user email: $email" }
    }
}
