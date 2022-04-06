package pt.isel.ls.sports.services.sections

import kotlinx.datetime.LocalDate
import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.errors.AppError
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

        val uid = authenticate(token)
        validateSportExists(sid)
        if (rid != null) validateRouteExists(rid)

        return db.activities.createNewActivity(uid, date, duration, sid, rid)
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

        return db.activities.getActivity(aid)
    }

    /**
     * Delete an activity.
     *
     * @param aid activity's unique identifier
     */
    fun deleteActivity(token: String, aid: Int) {
        validateAid(aid)

        val uid = authenticate(token)
        val activity = db.activities.getActivity(aid)

        if (uid != activity.uid)
            throw AppError.Forbidden("You are not allowed to delete this activity")

        return db.activities.deleteActivity(aid)
    }

    /**
     * Get a list with the activities, given the parameters.
     *
     * @param sid sport's identifier
     * @param orderBy order by duration time, only has two possible values - "ascending" or "descending"
     * @param date activity date (optional)
     * @param rid route's unique identifier (optional)
     * @param limit limits the number of results returned (optional)
     * @param skip skips the number of results provided (optional)
     *
     * @return list of activities identifiers
     */
    fun getActivities(
        sid: Int,
        orderBy: String,
        date: LocalDate?,
        rid: Int?,
        limit: Int?,
        skip: Int?
    ): List<Activity> {
        validateSid(sid)
        if (rid != null) validateRid(rid)

        if (limit != null && limit <= 0)
            throw AppError.InvalidArgument("Limit must be higher than 0")

        if (skip != null && if (limit != null) skip < limit else skip <= 0)
            throw AppError.InvalidArgument("Skip must be higher than 0 and less than limit")

        val order = SortOrder.parse(orderBy)
            ?: throw AppError.InvalidArgument("Order by must be either ascending or descending")

        return db.activities.getActivities(sid, order, date, rid, skip, limit)
    }
}
