package pt.isel.ls.sports.services.sections

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.services.AbstractServices
import pt.isel.ls.sports.services.isValidId

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
    fun createNewActivity(token: String, date: String, duration: String, sid: Int, rid: Int?): Int {
        val uid = authenticate(token)

        if (!Activity.isValidDate(date))
            throw AppError.invalidArgument("Date must be in the format yyyy-mm-dd")

        if (!Activity.isValidDuration(duration))
            throw AppError.invalidArgument("Duration must be in the format hh:mm:ss.fff")

        if (!isValidId(sid))
            throw AppError.invalidArgument("Sport id must be positive")

        if (rid != null && !isValidId(rid))
            throw AppError.invalidArgument("Route id must be positive")

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
        if (!isValidId(aid))
            throw AppError.invalidArgument("Activity id must be positive")

        return db.activities.getActivity(aid)
    }

    /**
     * Delete an activity.
     *
     * @param aid activity's unique identifier
     */
    fun deleteActivity(token: String, aid: Int) {
        val uid = authenticate(token)
        val activity = db.activities.getActivity(aid)

        if (uid != activity.uid)
            throw AppError.forbidden("You are not allowed to delete this activity")

        if (!isValidId(aid))
            throw AppError.invalidArgument("Activity id must be positive")

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
        date: String?,
        rid: Int?,
        limit: Int?,
        skip: Int?
    ): List<Activity> {
        if (!isValidId(sid))
            throw AppError.invalidArgument("Sport id must be positive")

        val order = SortOrder.parse(orderBy)
            ?: throw AppError.invalidArgument("Order by must be either ascending or descending")

        return db.activities.getActivities(sid, order, date, rid, skip, limit)
    }
}
