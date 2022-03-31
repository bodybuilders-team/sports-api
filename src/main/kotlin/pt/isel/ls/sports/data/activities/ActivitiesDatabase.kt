package pt.isel.ls.sports.data.activities

import pt.isel.ls.sports.data.SortOrder
import pt.isel.ls.sports.domain.Activity

/**
 * Activities database representation.
 */
interface ActivitiesDatabase {
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
    fun createNewActivity(uid: Int, date: String, duration: String, sid: Int, rid: Int? = null): Int

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
        date: String? = null,
        rid: Int? = null,
        skip: Int? = null,
        limit: Int? = null
    ): List<Activity>

    /**
     * Verifies if an activity exists with the given [aid]
     *
     * @param aid activity's unique identifier
     *
     * @return true if the activity exists, false otherwise
     */
    fun hasActivity(aid: Int): Boolean
}
