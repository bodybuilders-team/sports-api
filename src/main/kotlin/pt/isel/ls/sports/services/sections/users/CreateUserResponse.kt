package pt.isel.ls.sports.services.sections.users

/**
 * Represents a user creation response.
 *
 * @property token user's token
 * @property uid user's unique identifier
 */
data class CreateUserResponse(val token: String, val uid: Int)
