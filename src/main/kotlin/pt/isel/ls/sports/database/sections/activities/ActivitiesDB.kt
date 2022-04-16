package pt.isel.ls.sports.database.sections.activities

import kotlinx.datetime.LocalDate
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import kotlin.time.Duration

/**
 * Activities database representation.
 */
interface ActivitiesDB {
    /**
     * Create a new activity.
     *
     * @param conn database Connection
     * @param uid user's unique identifier
     * @param sid sport's unique identifier
     * @param duration
     * @param date
     * @param rid route's unique identifier
     *
     * @return activity's unique identifier
     */
    fun createNewActivity(
        conn: ConnectionDB,
        uid: Int,
        date: LocalDate,
        duration: Duration,
        sid: Int,
        rid: Int? = null
    ): Int

    /**
     * Get the detailed information of an activity.
     *
     * @param conn database Connection
     * @param aid activity's unique identifier
     */
    fun getActivity(conn: ConnectionDB, aid: Int): Activity

    /**
     * Delete an activity.
     *
     * @param conn database Connection
     * @param aid activity's unique identifier
     */
    fun deleteActivity(conn: ConnectionDB, aid: Int)

    /**
     * Get a list with the activities, given the parameters.
     *
     * @param conn database Connection
     * @param sid sport's identifier
     * @param orderBy order by duration time, only has two possible values - "ascending" or "descending"
     * @param date activity date (optional)
     * @param rid route's unique identifier (optional)
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return list of activities
     */
    fun searchActivities(
        conn: ConnectionDB,
        sid: Int,
        orderBy: SortOrder,
        date: LocalDate? = null,
        rid: Int? = null,
        skip: Int,
        limit: Int
    ): List<Activity>

    /**
     * Get a list with the users that have an activity, given the parameters.
     *
     * @param conn database Connection
     * @param sid sport's identifier
     * @param rid route's unique identifier
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return list of users
     */
    fun searchUsersByActivity(
        conn: ConnectionDB,
        sid: Int,
        rid: Int,
        skip: Int,
        limit: Int
    ): List<User>

    /**
     * Get all the activities of a sport.
     *
     * @param conn database Connection
     * @param sid sport's unique identifier
     *
     * @return list of identifiers of activities of a sport
     */
    fun getSportActivities(conn: ConnectionDB, sid: Int): List<Activity>

    /**
     * Get all the activities made from a user.
     *
     * @param conn database Connection
     * @param uid user's unique identifier
     *
     * @return list of identifiers of activities made from a user
     */
    fun getUserActivities(conn: ConnectionDB, uid: Int): List<Activity>

    /**
     * Verifies if an activity exists with the given [aid]
     *
     * @param conn database Connection
     * @param aid activity's unique identifier
     *
     * @return true if the activity exists, false otherwise
     */
    fun hasActivity(conn: ConnectionDB, aid: Int): Boolean
}
