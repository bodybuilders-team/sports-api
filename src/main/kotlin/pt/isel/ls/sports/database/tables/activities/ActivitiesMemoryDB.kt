package pt.isel.ls.sports.database.tables.activities

import pt.isel.ls.sports.database.memory.AppMemoryDBSource
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.errors.AppError

class ActivitiesMemoryDB(private val source: AppMemoryDBSource) : ActivitiesDB {

    override fun createNewActivity(uid: Int, date: String, duration: String, sid: Int, rid: Int?): Int {
        val id = source.nextActivityId.getAndIncrement()

        source.activities[id] = Activity(id = id, date, duration, uid, sid, rid)

        return id
    }

    override fun getActivity(aid: Int): Activity {
        return source.activities[aid] ?: throw AppError.NotFound("Activity with id $aid not found")
    }

    override fun deleteActivity(aid: Int) {
        source.activities.remove(aid) ?: throw AppError.NotFound("Activity with id $aid not found")
    }

    override fun getActivities(sid: Int, orderBy: SortOrder, date: String?, rid: Int?, skip: Int?, limit: Int?) =
        source.activities
            .filter {
                it.value.sid == sid &&
                    it.value.date == date &&
                    it.value.rid == rid
            }
            .values.toList()
            .sortedWith(
                if (orderBy == SortOrder.ASCENDING)
                    compareBy { it.duration }
                else
                    compareByDescending { it.duration }
            )

    override fun getSportActivities(sid: Int): List<Activity> {
        return source.activities.filter { it.value.sid == sid }.values.toList()
    }

    override fun getUserActivities(uid: Int): List<Activity> {
        return source.activities.filter { it.value.uid == uid }.values.toList()
    }

    override fun hasActivity(aid: Int): Boolean =
        source.activities.containsKey(aid)
}
