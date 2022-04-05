package pt.isel.ls.sports.database.tables.activities

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import kotlin.time.Duration

/**
 * Activities database representation.
 */
interface ActivitiesDB {
    /**
     * Create a new activity.
     *
     * @param uid user's unique identifier
     * @param sid sport's unique identifier
     * @param duration
     * @param date
     * @param rid route's unique identifier
     *
     * @return activity's unique identifier
     */
    fun createNewActivity(uid: Int, date: LocalDateTime, duration: Duration, sid: Int, rid: Int? = null): Int

    /**
     * Get the detailed information of an activity.
     *
     * @param aid activity's unique identifier
     */
    fun getActivity(aid: Int): Activity

    /**
     * Delete an activity.
     *
     * @param aid activity's unique identifier
     */
    fun deleteActivity(aid: Int)

    /**
     * Get a list with the activities, given the parameters.
     *
     * @param sid sport's identifier
     * @param orderBy order by duration time, only has two possible values - "ascending" or "descending"
     * @param date activity date (optional)
     * @param rid route's unique identifier (optional)
     *
     * @return list of activities
     */
    fun getActivities(
        sid: Int,
        orderBy: SortOrder,
        date: LocalDateTime? = null,
        rid: Int? = null,
        skip: Int? = null,
        limit: Int? = null
    ): List<Activity>

    /**
     * Get all the activities of a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return list of identifiers of activities of a sport
     */
    fun getSportActivities(sid: Int): List<Activity>

    /**
     * Get all the activities made from a user.
     *
     * @param uid user's unique identifier
     *
     * @return list of identifiers of activities made from a user
     */
    fun getUserActivities(uid: Int): List<Activity>

    /**
     * Verifies if an activity exists with the given [aid]
     *
     * @param aid activity's unique identifier
     *
     * @return true if the activity exists, false otherwise
     */
    fun hasActivity(aid: Int): Boolean
}
