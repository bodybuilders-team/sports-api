package pt.isel.ls.sports.services.sections.users

/**
 * Represents a login user response.
 *
 * @property token user token
 * @property uid user unique identifier
 */
data class LoginUserResponse(val token: String, val uid: Int)
