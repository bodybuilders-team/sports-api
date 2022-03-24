package pt.isel.ls.sports.domain

import kotlinx.serialization.Serializable


/**
 * Sport representation.
 *
 * @property id sport's unique identifier
 * @property name name of the sport
 * @property description description of the sport (optional)
 * @property uid unique identifier of the user who created the sport
 */
@Serializable
data class Sport(
	val id: Int,
	val name: String,
	val description: String = "",
	val uid: Int
)