package pt.isel.ls.sports.domain

import pt.isel.ls.sports.services.utils.isValidId

/**
 * Sport representation.
 *
 * @property id sport's unique identifier
 * @property name name of the sport
 * @property description description of the sport (optional)
 * @property uid unique identifier of the user who created the sport
 */
data class Sport(
    val id: Int,
    val name: String,
    val uid: Int,
    val description: String?
) {
    companion object {
        const val MIN_NAME_LENGTH = 3
        const val MAX_NAME_LENGTH = 30

        const val MIN_DESCRIPTION_LENGTH = 0
        const val MAX_DESCRIPTION_LENGTH = 1000

        /**
         * Checks if a name e valid.
         * @param name name to check
         * @return true if its valid
         */
        fun isValidName(name: String) =
            name.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH

        /**
         * Checks if a description e valid.
         * @param description description to check
         * @return true if its valid
         */
        fun isValidDescription(description: String) =
            description.length in MIN_DESCRIPTION_LENGTH..MAX_DESCRIPTION_LENGTH
    }

    init {
        require(isValidId(id)) { "Invalid sport id: $id" }
        require(isValidName(name)) { "Invalid name: $name" }
        if (description != null) require(isValidDescription(description)) { "Invalid description: $description" }
        require(isValidId(uid)) { "Invalid user id: $uid" }
    }
}
