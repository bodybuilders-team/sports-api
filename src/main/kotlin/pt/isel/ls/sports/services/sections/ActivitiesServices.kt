package pt.isel.ls.sports.services.sections

import kotlinx.datetime.LocalDate
import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.services.AbstractServices
import kotlin.time.Duration

class ActivitiesServices(db: AppDB) : AbstractServices(db) {
    /**
     * Create a new activity.
     *
     * @param token user's token
     * @param date
     * @param duration
     * @param sid sport's unique identifier
     * @param rid route's unique identifier
     *
     * @return activity's unique identifier
     */
    fun createNewActivity(token: String, date: LocalDate, duration: Duration, sid: Int, rid: Int?): Int {
        validateSid(sid)
        if (rid != null) validateRid(rid)

        return db.execute { conn ->
            val uid = authenticate(conn, token)
            validateSportExists(conn, sid)
            if (rid != null) validateRouteExists(conn, rid)

            db.activities.createNewActivity(conn, uid, date, duration, sid, rid)
        }
    }

    /**
     * Get an activity.
     *
     * @param aid activity's unique identifier
     *
     * @return the activity object
     */
    fun getActivity(aid: Int): Activity {
        validateAid(aid)

        return db.execute { conn ->
            db.activities.getActivity(conn, aid)
        }
    }

    /**
     * Delete an activity.
     *
     * @param aid activity's unique identifier
     */
    fun deleteActivity(token: String, aid: Int) {
        validateAid(aid)

        return db.execute { conn ->
            val uid = authenticate(conn, token)
            val activity = db.activities.getActivity(conn, aid)

            if (uid != activity.uid)
                throw AppException.Forbidden("You are not allowed to delete this activity")

            db.activities.deleteActivity(conn, aid)
        }
    }

    /**
     * Deletes a set of activities.
     *
     * @param activityIds activity's unique identifiers
     */
    fun deleteActivities(token: String, activityIds: Set<Int>) {
        if (activityIds.isEmpty())
            throw AppException.InvalidArgument("No activity ids were specified")

        activityIds.forEach(::validateAid)

        return db.execute { conn ->
            val uid = authenticate(conn, token)

            activityIds.forEach {
                val activity = db.activities.getActivity(conn, it)

                if (uid != activity.uid)
                    throw AppException.Forbidden("You are not allowed to delete activity with $it")

                db.activities.deleteActivity(conn, it)
            }
        }
    }

    /**
     * Searches for all activities that satisfy the given parameters.
     *
     * @param sid sport's identifier
     * @param orderBy order by duration time, only has two possible values - "ascending" or "descending"
     * @param date activity date (optional)
     * @param rid route's unique identifier (optional)
     * @param limit limits the number of results returned (optional)
     * @param skip skips the number of results provided (optional)
     *
     * @return list of activities
     */
    fun searchActivities(
        sid: Int,
        orderBy: String,
        date: LocalDate?,
        rid: Int?,
        skip: Int,
        limit: Int
    ): List<Activity> {
        validateSid(sid)
        if (rid != null) validateRid(rid)
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        val order = SortOrder.parse(orderBy)
            ?: throw AppException.InvalidArgument("Order by must be either ascending or descending")

        return db.execute { conn ->
            db.activities.searchActivities(conn, sid, order, date, rid, skip, limit)
        }
    }

    /**
     * Searches for all users that have an activity that satisfies the given parameters.
     *
     * @param sid sport's identifier
     * @param rid route's unique identifier (optional)
     * @param skip skips the number of results provided (optional)
     * @param limit limits the number of results returned (optional)
     *
     * @return list of activities identifiers
     */
    fun searchUsersByActivity(
        sid: Int,
        rid: Int,
        skip: Int,
        limit: Int
    ): List<User> {
        validateSid(sid)
        validateRid(rid)
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
