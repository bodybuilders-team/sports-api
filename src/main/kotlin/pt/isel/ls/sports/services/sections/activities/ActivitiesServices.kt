package pt.isel.ls.sports.services.sections.activities

import kotlinx.datetime.LocalDate
import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.database.sections.activities.ActivitiesResponse
import pt.isel.ls.sports.database.sections.activities.ActivitiesUsersResponse
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.services.AbstractServices
import pt.isel.ls.sports.services.exceptions.AuthenticationException
import pt.isel.ls.sports.services.exceptions.AuthorizationException
import kotlin.time.Duration

/**
 * Activities services. Implements methods regarding activities.
 */
class ActivitiesServices(db: AppDB) : AbstractServices(db) {

    /**
     * Creates a new activity.
     *
     * @param token user's token
     * @param date date of the activity
     * @param duration duration of the activity
     * @param sid sport's unique identifier
     * @param rid route's unique identifier (optional)
     *
     * @return activity's unique identifier
     * @throws InvalidArgumentException if [sid] is negative
     * @throws InvalidArgumentException if [rid] is negative
     * @throws AuthenticationException if a user with the [token] was not found
     * @throws InvalidArgumentException if there's no sport with the [sid]
     * @throws InvalidArgumentException if there's no route with the [rid]
     */
    fun createNewActivity(token: String, date: LocalDate, duration: Duration, sid: Int, rid: Int?): Int {
        validateSid(sid)
        if (rid != null) validateRid(rid)

        return db.execute { conn ->
            val uid = authenticate(conn, token)

            if (!db.sports.hasSport(conn, sid))
                throw InvalidArgumentException("There's no sport with the Sport id $sid")

            if (rid != null && !db.routes.hasRoute(conn, rid))
                throw InvalidArgumentException("There's no route with the Route id $rid")

            db.activities.createNewActivity(conn, uid, date, duration, sid, rid)
        }
    }

    /**
     * Updates an activity.
     *
     * @param id activity's unique identifier
     * @param token user's token
     * @param date new date of the activity
     * @param duration new duration of the activity
     * @param sid new sport id of the activity
     * @param rid new route id of the activity
     *
     * @return true if the activity was successfully modified, false otherwise
     * @throws InvalidArgumentException if [id] is negative
     * @throws InvalidArgumentException if [sid] is negative
     * @throws InvalidArgumentException if [rid] is negative
     * @throws AuthenticationException if a user with the [token] was not found
     * @throws InvalidArgumentException if there's no sport with the [sid]
     * @throws InvalidArgumentException if there's no route with the [rid]
     * @throws NotFoundException if there's no activity with the [id]
     * @throws AuthorizationException if the user with the [token] is not the owner of the activity
     * @throws InvalidArgumentException if [date], [duration], [sid] and [rid] are both null
     */
    fun updateActivity(id: Int, token: String, date: LocalDate?, duration: Duration?, sid: Int?, rid: Int?): Boolean {
        validateAid(id)
        if (sid != null) validateSid(sid)
        if (rid != null) validateRid(rid)

        return db.execute { conn ->
            val uid = authenticate(conn, token)

            if (sid != null && !db.sports.hasSport(conn, sid))
                throw InvalidArgumentException("There's no sport with the Sport id $sid")

            if (rid != null && !db.routes.hasRoute(conn, rid))
                throw InvalidArgumentException("There's no route with the Route id $rid")

            val activity = db.activities.getActivity(conn, id)
            if (activity.uid != uid)
                throw AuthorizationException("You are not allowed to update this activity.")

            db.activities.updateActivity(conn, id, date, duration, sid, rid)
        }
    }

    /**
     * Gets a specific activity.
     *
     * @param id activity's unique identifier
     *
     * @return the activity object
     * @throws InvalidArgumentException if [id] is negative
     * @throws NotFoundException if there's no activity with the [id]
     */
    fun getActivity(id: Int): Activity {
        validateAid(id)

        return db.execute { conn ->
            db.activities.getActivity(conn, id)
        }
    }

    /**
     * Deletes an activity.
     *
     * @param token user's token
     * @param id activity's unique identifier
     *
     * @throws InvalidArgumentException if [id] is negative
     * @throws AuthenticationException if a user with the [token] was not found
     * @throws NotFoundException if there's no activity with the [id]
     * @throws AuthorizationException if the user with the [token] is not the owner of the activity
     */
    fun deleteActivity(token: String, id: Int) {
        validateAid(id)

        db.execute { conn ->
            val uid = authenticate(conn, token)

            val activity = db.activities.getActivity(conn, id)
            if (uid != activity.uid)
                throw AuthorizationException("You are not allowed to delete this activity")

            db.activities.deleteActivity(conn, id)
        }
    }

    /**
     * Deletes a set of activities.
     *
     * @param token user's token
     * @param activityIds set of activities' unique identifiers
     *
     * @throws InvalidArgumentException if [activityIds] is empty
     * @throws InvalidArgumentException if [activityIds] contains negative values
     * @throws AuthenticationException if a user with the [token] was not found
     * @throws NotFoundException if some activity identifier doesn't match any activity
     * @throws AuthorizationException if the user with the [token] is not the owner of some activity
     */
    fun deleteActivities(token: String, activityIds: Set<Int>) {
        if (activityIds.isEmpty())
            throw InvalidArgumentException("No activity ids were specified")

        activityIds.forEach(::validateAid)

        db.execute { conn ->
            val uid = authenticate(conn, token)

            activityIds.forEach {
                val activity = db.activities.getActivity(conn, it)

                if (uid != activity.uid)
                    throw AuthorizationException("You are not allowed to delete activity $it")

                db.activities.deleteActivity(conn, it)
            }
        }
    }

    /**
     * Searches for all activities that satisfy the given parameters.
     *
     * @param sid sport's identifier
     * @param orderBy order by duration time, only has two possible values - "ascending" or "descending"
     * @param date date of the activity (optional)
     * @param rid route's unique identifier (optional)
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [ActivitiesResponse] with the list of activities
     * @throws InvalidArgumentException if [sid] is negative
     * @throws InvalidArgumentException if [rid] is negative
     * @throws InvalidArgumentException if [skip] is invalid
     * @throws InvalidArgumentException if [limit] is invalid
     * @throws InvalidArgumentException if [orderBy] is not "ascending" or "descending"
     */
    fun searchActivities(
        sid: Int,
        orderBy: String,
        date: LocalDate?,
        rid: Int?,
        skip: Int,
        limit: Int
    ): ActivitiesResponse {
        validateSid(sid)
        if (rid != null) validateRid(rid)
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        val order = SortOrder.parse(orderBy)
            ?: throw InvalidArgumentException("Order by must be either \"ascending\" or \"descending\"")

        return db.execute { conn ->
            db.activities.searchActivities(conn, sid, order, date, rid, skip, limit)
        }
    }

    /**
     * Searches for all users that have an activity that satisfies the given parameters.
     *
     * @param sid sport's identifier
     * @param rid route's unique identifier (optional)
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [ActivitiesUsersResponse] with the list of users
     * @throws InvalidArgumentException if [sid] is negative
     * @throws InvalidArgumentException if [rid] is negative
     * @throws InvalidArgumentException if [skip] is invalid
     * @throws InvalidArgumentException if [limit] is invalid
     * @throws NotFoundException if there's an activity whose user identifier doesn't match any user
     */
    fun searchUsersByActivity(
        sid: Int,
        rid: Int?,
        skip: Int,
        limit: Int
    ): ActivitiesUsersResponse {
        validateSid(sid)
        if (rid != null) validateRid(rid)
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        return db.execute { conn ->
            db.activities.searchUsersByActivity(conn, sid, rid, skip, limit)
        }
    }

    companion object {
        private val LIMIT_RANGE = 0..100
    }
}
