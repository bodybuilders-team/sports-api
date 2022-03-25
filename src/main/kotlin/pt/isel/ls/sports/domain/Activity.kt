package pt.isel.ls.sports.domain

import kotlinx.serialization.Serializable

/**
 * Activity representation.
 *
 * @property id activity unique identifier
 * @property date activity date
 * @property duration duration of the activity
 * @property uid unique identifier of the user who created the activity
 * @property sid unique identifier of the activity sport
 * @property rid unique identifier of the activity route (optional)
 */
@Serializable
data class Activity(
    val id: Int,
    val date: String,
    val duration: String,
    val uid: Int,
    val sid: Int,
    val rid: Int?
)
