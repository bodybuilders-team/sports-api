package pt.isel.ls.sports.domain

import kotlinx.datetime.LocalDate
import pt.isel.ls.sports.services.utils.isValidId
import kotlin.time.Duration

/**
 * Activity representation.
 *
 * @property id activity's unique identifier
 * @property date date of the activity
 * @property duration duration of the activity
 * @property uid unique identifier of the user who created the activity
 * @property sid unique identifier of the activity sport
 * @property rid unique identifier of the activity route (optional)
 */
data class Activity(
    val id: Int,
    val date: LocalDate,
    val duration: Duration,
    val uid: Int,
    val sid: Int,
    val rid: Int? = null
) {
    init {
        require(isValidId(id)) { "Invalid activity id: $id" }
        require(isValidId(uid)) { "Invalid activity user id: $uid" }
        require(isValidId(sid)) { "Invalid activity sport id: $sid" }
        if (rid != null) require(isValidId(rid)) { "Invalid activity route id: $rid" }
    }
}
