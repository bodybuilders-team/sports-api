package pt.isel.ls.sports.database.sections.activities

import kotlinx.datetime.LocalDate
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.database.sections.users.UsersResponse
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import kotlin.time.Duration

/**
 * Activities database representation.
 */
interface ActivitiesDB {

    /**
     * Creates a new activity.
     *
     * @param conn database Connection
     * @param uid user's unique identifier
     * @param date date of the activity
     * @param duration duration of the activity
     * @param sid sport's unique identifier
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
     * Updates an activity.
     *
     * @param conn database Connection
     * @param aid activity's unique identifier
     * @param date new date of the activity
     * @param duration new duration of the activity
     * @param sid new sport id of the activity
     * @param rid new route id of the activity
     *
     * @return true if the activity was modified, false otherwise
     * @throws NotFoundException if there's no activity with the [aid]
     * @throws InvalidArgumentException if [date], [duration], [sid] and [rid] are both null
     */
    fun updateActivity(
        conn: ConnectionDB,
        aid: Int,
        date: LocalDate?,
        duration: Duration?,
        sid: Int?,
        rid: Int?
    ): Boolean

    /**
     * Gets the detailed information of an activity.
     *
     * @param conn database Connection
     * @param aid activity's unique identifier
     *
     * @return the activity object
     * @throws NotFoundException if there's no activity with the [aid]
     */
    fun getActivity(conn: ConnectionDB, aid: Int): Activity

    /**
     * Deletes an activity.
     *
     * @param conn database Connection
     * @param aid activity's unique identifier
     *
     * @throws NotFoundException if there's no activity with the [aid]
     */
    fun deleteActivity(conn: ConnectionDB, aid: Int)

    /**
     * Searches for all activities that satisfy the given parameters.
     *
     * @param conn database Connection
     * @param sid sport's identifier
     * @param orderBy order by duration time, only has two possible values - "ascending" or "descending"
     * @param date date of the activity (optional)
     * @param rid route's unique identifier (optional)
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [ActivitiesResponse] with the list of activities
     */
    fun searchActivities(
        conn: ConnectionDB,
        sid: Int,
        orderBy: SortOrder,
        date: LocalDate?,
        rid: Int?,
        skip: Int,
        limit: Int
    ): ActivitiesResponse

    /**
     * Searches for all users that have an activity that satisfies the given parameters.
     *
     * @param conn database Connection
     * @param sid sport's identifier
     * @param rid route's unique identifier
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return list of users
     * @throws NotFoundException if there's an activity whose user identifier doesn't match any user
     */
    fun searchUsersByActivity(
        conn: ConnectionDB,
        sid: Int,
        rid: Int?,
        skip: Int,
        limit: Int
    ): UsersResponse

    /**
     * Gets all activities of a specific sport.
     *
     * @param conn database Connection
     * @param sid sport's unique identifier
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [ActivitiesResponse] with the list of activities
     */
    fun getSportActivities(
        conn: ConnectionDB,
        sid: Int,
        skip: Int,
        limit: Int
    ): ActivitiesResponse

    /**
     * Gets all the activities made by a specific user.
     *
     * @param conn database Connection
     * @param uid user's unique identifier
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [ActivitiesResponse] with the list of activities
     */
    fun getUserActivities(
        conn: ConnectionDB,
        uid: Int,
        skip: Int,
        limit: Int
    ): ActivitiesResponse

    /**
     * Verifies if an activity with the given [aid] exists.
     *
     * @param conn database Connection
     * @param aid activity's unique identifier
     *
     * @return true if the activity exists, false otherwise
     */
    fun hasActivity(conn: ConnectionDB, aid: Int): Boolean
}
