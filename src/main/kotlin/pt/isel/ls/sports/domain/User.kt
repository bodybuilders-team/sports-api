package pt.isel.ls.sports.domain

import pt.isel.ls.sports.services.utils.isValidId
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
 * User representation.
 *
 * @property id user's unique identifier
 * @property name name of the user
 * @property email email of the user
 * @property password (hashed) password of the user
 */
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
) {
    companion object {
        const val MIN_NAME_LENGTH = 3
        const val MAX_NAME_LENGTH = 60

        private const val HASHED_PASSWORD_LENGTH = 64

        private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"

        /**
         * Checks if an email is valid.
         *
         * @param email email to check
         * @return true if it's valid
         */
        fun isValidEmail(email: String) =
            email.matches(Regex(EMAIL_REGEX))

        /**
         * Checks if a name is valid.
         *
         * @param name name to check
         * @return true if it's valid
         */
        fun isValidName(name: String) =
            name.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH

        /**
         * Hashes a password (preferrably with salt) using the SHA-256 algorithm.
         *
         * @param password password to hash
         * @return hashed password
         */
        fun hashPassword(password: String): String {
            // Hash using SHA-256
            val digest = MessageDigest.getInstance("SHA-256")
            val encodedhash = digest.digest(
                password.toByteArray(StandardCharsets.UTF_8)
            )

            // Convert to hexadecimal
            val sb = StringBuilder()
            for (b in encodedhash) {
                val hex = Integer.toHexString(b.toInt())
                if (hex.length == 1) {
                    sb.append('0')
                }
                sb.append(hex)
            }

            return sb.toString().substring(0 until HASHED_PASSWORD_LENGTH)
        }

        /**
         * Checks if the given [password] is the same as the user's,
         * by comparing the stored hash to the hash of the [password].
         *
         * @param password password to check
         * @return true if it's the same as the user's
         */
        fun checkPassword(password: String, hash: String) =
            hashPassword(password) == hash
    }

    init {
        require(isValidId(id)) { "Invalid user id: $id" }
        require(isValidName(name)) { "Invalid user name: $name" }
        require(isValidEmail(email)) { "Invalid user email: $email" }
    }
}
