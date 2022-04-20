package pt.isel.ls.sports.database.sections.activities

import kotlinx.datetime.LocalDate
import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppException
import kotlin.time.Duration

class ActivitiesMemoryDB(private val source: AppMemoryDBSource) : ActivitiesDB {

    override fun createNewActivity(
        conn: ConnectionDB,
        uid: Int,
        date: LocalDate,
        duration: Duration,
        sid: Int,
        rid: Int?
    ): Int {
        val id = source.nextActivityId.getAndIncrement()

        source.activities[id] = Activity(id, date, duration, uid, sid, rid)

        return id
    }

    override fun getActivity(conn: ConnectionDB, aid: Int): Activity {
        return source.activities[aid] ?: throw AppException.NotFound("Activity with id $aid not found")
    }

    override fun deleteActivity(conn: ConnectionDB, aid: Int) {
        source.activities.remove(aid) ?: throw AppException.NotFound("Activity with id $aid not found")
    }

    override fun searchActivities(
        conn: ConnectionDB,
        sid: Int,
        orderBy: SortOrder,
        date: LocalDate?,
        rid: Int?,
        skip: Int,
        limit: Int
    ) =
        source.activities
            .filter {
                it.value.sid == sid &&
                    (date == null || it.value.date == date) &&
                    (rid == null || it.value.rid == rid)
            }
            .values.toList()
            .sortedWith(
                if (orderBy == SortOrder.ASCENDING)
                    compareBy { it.duration }
                else
                    compareByDescending { it.duration }
            )

    override fun searchUsersByActivity(
        conn: ConnectionDB,
        sid: Int,
        rid: Int,
        skip: Int,
        limit: Int
    ): List<User> {
        return source.activities
            .filter {
                it.value.sid == sid && it.value.rid == rid
            }
            .values.toList()
            .sortedWith(
                compareBy { it.duration }
            )
            .map {
                source.users[it.uid] ?: throw AppException.NotFound("User with id ${it.uid} not found")
            }
            .distinct()
    }

    override fun getSportActivities(conn: ConnectionDB, sid: Int): List<Activity> {
        return source.activities.filter { it.value.sid == sid }.values.toList()
    }

    override fun getUserActivities(conn: ConnectionDB, uid: Int): List<Activity> {
        return source.activities.filter { it.value.uid == uid }.values.toList()
    }

    override fun hasActivity(conn: ConnectionDB, aid: Int): Boolean =
        source.activities.containsKey(aid)
}
