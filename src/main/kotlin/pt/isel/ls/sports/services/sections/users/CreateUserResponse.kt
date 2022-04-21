package pt.isel.ls.sports.services.sections.users

/**
 * Response for the createUser method of services.
 *
 * @property token user's token
 * @property uid user's unique identifier
 */
data class CreateUserResponse(val token: String, val uid: Int)
