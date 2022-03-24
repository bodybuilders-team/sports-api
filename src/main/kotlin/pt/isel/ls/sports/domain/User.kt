package pt.isel.ls.sports.domain

import kotlinx.serialization.Serializable

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
)
